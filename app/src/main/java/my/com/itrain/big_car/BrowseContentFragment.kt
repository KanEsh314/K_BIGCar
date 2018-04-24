package my.com.itrain.big_car

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_trips_content.*

/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap:GoogleMap
//    var VehicleType = arrayOf<String>("4 Seats", "10 Seats", "12 Seats", "16 Seats", "6 Seats")

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_browse_content, container, false)

//        val vehicle_type_spinner = rootView.findViewById<Spinner>(R.id.vehicle_spinner)
//        var adapter = ArrayAdapter<String>(context, R.layout.vehicle_type, R.id.vehicle_text, VehicleType)
//        vehicle_type_spinner.adapter = adapter

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return rootView
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
                        latitude = task.result.latitude
                        longitude = task.result.longitude
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
        val snackbar = Snackbar.make(coordinateLayout, getString(snackStrId),
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
