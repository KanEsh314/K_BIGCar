package my.com.itrain.big_car


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import android.support.design.widget.Snackbar.LENGTH_INDEFINITE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_trips_content.*
import my.com.itrain.big_car.BuildConfig.APPLICATION_ID
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class TripsContentFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var nearbyURL = "https://gentle-atoll-11837.herokuapp.com/api/tripnearby/"
    private val nearByMaterial = ArrayList<JSONObject>()
    private lateinit var myLocation: LatLng
    private var service_id: Int = 0
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trips_content, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return view
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermission()
        } else {
            getLastLocation()
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_COARSE_LOCATION)) {
            Log.i("Info", "Displaying permission rationale to provide additional context.")
        } else {
            Log.i("Info", "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> getLastLocation()
                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                            View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                val intent = Intent().apply {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts("package", APPLICATION_ID, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                startActivity(intent)
                            })
                }
            }
        }
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener{
            task ->
                if (task.isSuccessful && task.result != null){

                    val requestVolley = Volley.newRequestQueue(this.context)

                    val progressDialog = ProgressDialog(context, R.style.DialogTheme)
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressDialog.setTitle("Please Wait")
                    progressDialog.setMessage("Loading")
                    progressDialog.show()

                    myLocation = LatLng(task.result.latitude, task.result.longitude)

                    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, nearbyURL+task.result.latitude+","+task.result.longitude, null, object : Response.Listener<JSONObject>{
                        override fun onResponse(response: JSONObject) {
                            val nearbyData = response.getJSONArray("data")

                            for (i in 0 until nearbyData.length()){
                                Log.d("Debug", nearbyData.toString())
                                nearByMaterial.add(nearbyData.getJSONObject(i))
                            }
                            progressDialog.dismiss()
                        }
                    },
                            object : Response.ErrorListener{
                                override fun onErrorResponse(error: VolleyError) {
                                    error.printStackTrace()
                                    progressDialog.dismiss()
                                }
                            })
                    requestVolley.add(jsonObjectRequest)

                }else{
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
                LENGTH_INDEFINITE)
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }

    private fun checkPermissions() =
            ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    override fun onMapReady(map:GoogleMap) {
        mMap = map
        mMap.isMyLocationEnabled
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.setOnMapLoadedCallback(object : GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                for (i in 0 until nearByMaterial.size){
                    val nerByMap = mMap.addMarker(MarkerOptions()
                                    .position(LatLng(nearByMaterial.get(i).getDouble("latitude"), nearByMaterial.get(i).getDouble("longitude")))
                                    .title(nearByMaterial.get(i).getString("attraction")))
                    nerByMap.tag = nearByMaterial.get(i).getInt("service_id")
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18F))
                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
                    override fun onMarkerClick(p0: Marker): Boolean {
                        service_id = p0.tag as Int
                        val intent = Intent(context,TourDetailActivity::class.java)
                        try {
                            intent.putExtra("service_id", service_id)
                        }catch (e : JSONException){
                            e.printStackTrace()
                        }
                        startActivity(intent)
                        return true
                    }
                })
            }
        })
    }
}// Required empty public constructor
