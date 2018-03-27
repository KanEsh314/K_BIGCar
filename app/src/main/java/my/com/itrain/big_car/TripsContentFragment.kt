package my.com.itrain.big_car


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import java.nio.file.Files.move


/**
 * A simple [Fragment] subclass.
 */
class TripsContentFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private lateinit var mMap: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trips_content, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        googleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        return view
    }

    override fun onMapReady(map:GoogleMap) {
        mMap = map
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap.setOnMapLoadedCallback(object : GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                val myLocation = LatLng(3.157764, 101.711860)
                mMap.addMarker(MarkerOptions().position(myLocation).draggable(true))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,12F))
                mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
                    override fun onMarkerClick(p0: Marker?): Boolean {
                        val intent = Intent(context,TourDetailActivity::class.java)
                        try {
                            intent.putExtra("serviceid", 1)
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

    private fun getCurrentLocation(){
        mMap.clear()

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return
        }

        val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (location != null){
            latitude = location.latitude
            longitude = location.longitude

            moveMap()
        }
    }

    private fun moveMap() {
        val latLng = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(latLng).draggable(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.uiSettings.isZoomControlsEnabled
    }

    override fun onConnectionSuspended(p0: Int) {
        getCurrentLocation()
    }

    override fun onConnected(p0: Bundle?) {
        getCurrentLocation()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("Debug", p0.toString())
    }
}// Required empty public constructor
