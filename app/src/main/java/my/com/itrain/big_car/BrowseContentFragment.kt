package my.com.itrain.big_car

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.book_content.*
import kotlinx.android.synthetic.main.fragment_browse_content.*
import my.com.itrain.big_car.R.id.textView
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment(), OnMapReadyCallback {

    var vehicleTypeURL = "https://gentle-atoll-11837.herokuapp.com/api/vehicletypes"
    var tripURL = "https://gentle-atoll-11837.herokuapp.com/api/attractionbooking"

    private lateinit var mMap:GoogleMap
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val SECOND_ACTIVITY__PAYMENT_REQUEST_CODE = 2
    private val SECOND_ACTIVITY__TYPE_REQUEST_CODE = 1
    private val SECOND_ACTIVITY__DROP_REQUEST_CODE = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    val vehicleMaterial = ArrayList<JSONObject>()

    var pick_address:String = ""
    var vehicle_type_id:String = ""
    var trip_type_id:String = ""
    var trip_pay_id:String = ""
    var drop_off_id:String = ""
    var trip_time:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_browse_content, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dropoff.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(activity, PlaceActivity::class.java)
                try {
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                }catch (e: JSONException){
                    e.printStackTrace()
                }
                startActivityForResult(intent, SECOND_ACTIVITY__DROP_REQUEST_CODE)
            }
        })

        tripPayment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(activity, PaymentActivity::class.java)
                startActivityForResult(intent, SECOND_ACTIVITY__PAYMENT_REQUEST_CODE)
            }
        })

        tripTime.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val calender = Calendar.getInstance()
                val mYear = calender.get(Calendar.YEAR)
                val mMonth = calender.get(Calendar.MONTH)
                val mDay = calender.get(Calendar.DAY_OF_MONTH)
                val mHour = calender.get(Calendar.HOUR_OF_DAY)
                val mMinute = calender.get(Calendar.MINUTE)

                val datePickerDialog = DatePickerDialog(context, R.style.DialogTheme, object: DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view:DatePicker, year:Int, monthOfYear:Int, dayOfMonth:Int) {
                        Log.d("Debug", dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        val timePickerDialog = TimePickerDialog(context, R.style.DialogTheme, object: TimePickerDialog.OnTimeSetListener{
                            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                                Log.d("Debug", hourOfDay.toString()+ ":" + minute)
                                trip_time = year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth.toString()+" "+hourOfDay+":"+minute
                                tripTime?.text = trip_time
                            }
                        }, mHour, mMinute, true)
                        timePickerDialog.show()
                    }
                }, mYear, mMonth, mDay)
                datePickerDialog.show()


            }
        })

        tripType.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(activity, TypeActivity::class.java)
                startActivityForResult(intent, SECOND_ACTIVITY__TYPE_REQUEST_CODE)
            }
        })

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading")
        progressDialog.show()

        val requestVolley = Volley.newRequestQueue(activity)

        val vehicleAdapter = VehicleAdapter(context)
        vehicleType.adapter = vehicleAdapter
        vehicleType.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vehicle_type_id = vehicleMaterial.get(position).getString("vehicletype_id")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        var jsonObjectRequestTour = JsonObjectRequest(Request.Method.GET, vehicleTypeURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val toursData = response.getJSONArray("data")

                for (i in 0 until toursData.length()){
                    vehicleMaterial.add(toursData.getJSONObject(i))
                    vehicleAdapter.addJsonObject(toursData.getJSONObject(i))
                }
                vehicleAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            } catch (e : JSONException){
                e.printStackTrace()
                progressDialog.dismiss()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                        progressDialog.dismiss()
                    }
                })

        requestVolley.add(jsonObjectRequestTour)

        selectTrip.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                val sharedPreferences = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
                val stringRequest = object : StringRequest(Request.Method.POST, tripURL, object : Response.Listener<String> {
                    override fun onResponse(response: String) {
                        Log.d("Debug", response)
                        progressDialog.dismiss()
                        Toast.makeText(activity, "Successfully Addded", Toast.LENGTH_LONG).show()
                    }
                },
                        object : Response.ErrorListener {
                            override fun onErrorResponse(error: VolleyError?) {
                                //Log.d("Error", error.toString())
                                progressDialog.dismiss()
                                //Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                            }

                        }){
                    @Throws(AuthFailureError::class)
                    override fun getHeaders():Map<String,String>{
                        val headers = HashMap<String, String>()
                        headers.put("Authorization", "Bearer "+sharedPreferences)
                        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                        return headers
                    }
                    override fun getParams():Map<String, String> {
                        val params = HashMap<String, String>()

                        params.put("address", pick_address)
                        params.put("attraction", drop_off_id.toString())
                        params.put("trip_type", trip_type_id.toString())
                        params.put("payment_method", trip_pay_id.toString())
                        params.put("remarks", trip_driver_notes.text.toString())
                        params.put("datetime", trip_time)
                        params.put("vehicle_type", vehicle_type_id.toString())

                        Log.d("Debug",params.toString())
                        return params
                    }
                }

                requestVolley.add(stringRequest)

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SECOND_ACTIVITY__TYPE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                tripType.text = data.getStringExtra("selectedTypeName")
                 trip_type_id = data.getStringExtra("selectedTypeId")
            }
        }

        if (requestCode == SECOND_ACTIVITY__DROP_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                dropoff.text = data.getStringExtra("selectedDropName")
                drop_off_id = data.getStringExtra("selectedDropId")
            }
        }

        if (requestCode == SECOND_ACTIVITY__PAYMENT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                tripPayment.text = data.getStringExtra("selectedPayName")
                trip_pay_id = data.getStringExtra("selectedPayId")
            }
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.i("Info", "Displaying permission rationale to provide additional context.")
        } else {
            Log.i("Info", "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        val geoCoder = Geocoder(activity)
        var addresses: List<Address>?
        var address: Address?

        fusedLocationClient.lastLocation
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful && task.result != null) {
                        latitude = task.result.latitude
                        longitude = task.result.longitude
                        try {
                            addresses = geoCoder.getFromLocation(task.result.latitude, task.result.longitude, 1)
                            if (null != addresses && !addresses!!.isEmpty()) {
                                address = addresses!![0]
                                pick_address = address!!.getAddressLine(0)
                                pickup?.text = pick_address
                            }
                        }catch (e: IOException){
                            e.printStackTrace()
                        }
                    } else {
                        Log.d("Debug","getLastLocation:exception"+task.exception)
                        showSnackbar(R.string.no_location_detected)
                    }
                }
    }

    private fun showSnackbar(
            snackStrId: Int,
            actionStrId: Int = 0,
            listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(relativeLayout, getString(snackStrId),
                Snackbar.LENGTH_INDEFINITE)
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }

    private fun checkPermissions() =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        map.isMyLocationEnabled = true
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.setOnMapLoadedCallback (object : GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                //mMap.addMarker(MarkerOptions().position(LatLng(latitude,longitude)).title("myLocation"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude,longitude),18F))

            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermission()
        } else {
            getLastLocation()
        }
    }
}// Required empty public constructor

class VehicleAdapter(private val context: Context) : BaseAdapter() {

    private val vehicleType = ArrayList<JSONObject>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.trip_type, parent, false)
        val vTitle = view.findViewById<TextView>(R.id.vehicleTitle)
        vTitle.text = vehicleType.get(position).getString("type")
        val vIcon = view.findViewById<ImageView>(R.id.vehicleIcon)
        vIcon.setImageResource(R.mipmap.ic_action_seats)
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return vehicleType.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        vehicleType.add(jsonObject)
    }

}
