package my.com.itrain.big_car

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.book_content.*
import kotlinx.android.synthetic.main.fragment_browse_content.*
import org.json.JSONObject
import java.util.*
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap:GoogleMap
    private val PLACE_AUTO_COMPLETE_PICK_CODE = 0
    private var pickLatLng: LatLng = LatLng(0.0,0.0)
    private val PLACE_AUTO_COMPLETE_DROP_CODE = 1
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val SECOND_ACTIVITY_REQUEST_CODE = 0
    private val typeMaterial = ArrayList<String>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude:Double = 4.2105
    private var longitude:Double = 101.9758

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

        selectTrip.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val alertDialog = AlertDialog.Builder(context,R.style.DialogTheme)
                val inflater = layoutInflater
                val dialog =inflater.inflate(R.layout.trip_type, null)
                dialog.minimumHeight = resources.displayMetrics.heightPixels/2
                dialog.minimumWidth = resources.displayMetrics.widthPixels
                alertDialog.setView(dialog)
                        .create()
                        .show()
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
                                tripTime?.text = year.toString()+" "+(monthOfYear+1)+" "+dayOfMonth.toString()+" "+hourOfDay+" "+minute
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
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                val returnString = data?.getStringExtra("keyName")
                tripType.text = "Debug"
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
        fusedLocationClient.lastLocation
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful && task.result != null) {
                        //latitude = task.result.latitude
                        //longitude = task.result.longitude
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

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        map.isMyLocationEnabled
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.setOnMapLoadedCallback (object : GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                mMap.addMarker(MarkerOptions().position(LatLng(latitude,longitude)).title("myLocation"))
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
