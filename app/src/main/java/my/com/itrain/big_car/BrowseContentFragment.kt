package my.com.itrain.big_car

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
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
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.book_content.*
import kotlinx.android.synthetic.main.fragment_browse_content.*
import my.com.itrain.big_car.R.drawable.splash_screen
import my.com.itrain.big_car.R.id.*
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

    private var pick_Latitude:Double = 0.0
    private var pick_Longitude:Double = 0.0
    private var drop_Latitude:Double = 0.0
    private var drop_Longitude:Double = 0.0

    val vehicleMaterial = ArrayList<JSONObject>()

    var pick_address:String = ""
    var drop_address: String = ""
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
                    intent.putExtra("latitude", pick_Latitude)
                    intent.putExtra("longitude", pick_Longitude)
                }catch (e: JSONException){
                    e.printStackTrace()
                }
                startActivityForResult(intent, SECOND_ACTIVITY__DROP_REQUEST_CODE)
            }
        })

        tripPayment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(activity, PaymentActivity::class.java)
                try{
                    startActivityForResult(intent, SECOND_ACTIVITY__PAYMENT_REQUEST_CODE)
                }catch (e: IOException){
                    e.printStackTrace()
                }
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
                        progressDialog.dismiss()
                        startActivity(Intent(activity, DriverActivity::class.java))
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

                stringRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
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
            } else if (resultCode == RESULT_CANCELED){
                //Empty
            }
        }

        if (requestCode == SECOND_ACTIVITY__DROP_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                drop_address = data.getStringExtra("selectedDropName")
                dropoff.text = drop_address
                drop_off_id = data.getStringExtra("selectedDropId")
                drop_Latitude = data.getDoubleExtra("selectedLat", 0.0)
                drop_Longitude = data.getDoubleExtra("selectedLng", 0.0)
            } else if (resultCode == RESULT_CANCELED){
                //Empty
            }
        }

        if (requestCode == SECOND_ACTIVITY__PAYMENT_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                tripPayment.text = data.getStringExtra("selectedPayName")
                trip_pay_id = data.getStringExtra("selectedPayId")
            } else if (resultCode == RESULT_CANCELED){
                //Empty
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
                        pick_Latitude = task.result.latitude
                        pick_Longitude = task.result.longitude
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

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(pick_Latitude,pick_Longitude),15F))

                dropoff.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(pick_Latitude,pick_Longitude),15F))
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        onDestroy()
                        mMap.clear()
                    }

                    override fun afterTextChanged(s: Editable?) {

                        Handler().postDelayed({

                            val locations = ArrayList<PICKDROP>()
                            locations.add(PICKDROP(LatLng(pick_Latitude, pick_Longitude), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE), pick_address))
                            locations.add(PICKDROP(LatLng(drop_Latitude, drop_Longitude), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED), drop_address))
                            Log.d("Debug", locations.get(1).pick_drop_location.toString())
                            for (i in 0 until locations.size){
                                val pick_drop = mMap.addMarker(MarkerOptions().position(locations.get(i).pick_drop_location).title(locations.get(i).pick_drop_text).icon(locations.get(i).pick_drop_img))
                                pick_drop.showInfoWindow()
                            }

                            val builder = LatLngBounds.Builder()
                            builder.include(locations.get(0).pick_drop_location) //Taking Point A (First LatLng)
                            builder.include(locations.get(1).pick_drop_location) //Taking Point B (Second LatLng)
                            val bounds = builder.build()
                            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                            mMap.moveCamera(cu)
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null)

                        },250)
                    }
                })
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

class PICKDROP(val pick_drop_location: LatLng, val pick_drop_img: BitmapDescriptor, val pick_drop_text: String)

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
