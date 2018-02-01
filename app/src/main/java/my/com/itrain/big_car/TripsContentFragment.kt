package my.com.itrain.big_car


import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_trips_content.*


/**
 * A simple [Fragment] subclass.
 */
class TripsContentFragment : Fragment() {

    lateinit var mMapView:MapView
    lateinit var googleMap:GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trips_content, container, false)

        //map.onCreate(savedInstanceState)
        map.onResume()

        try{
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        map.getMapAsync(object: OnMapReadyCallback {
            override fun onMapReady(mMap: GoogleMap?) {
                googleMap = mMap!!
                Log.d("debug","map ready called")

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                            0)

                    Log.d("debug", "problem")

                }

                googleMap.setMyLocationEnabled(true)
                val sydney = LatLng(3.139003, 101.68685499999992)
                googleMap.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))
                val cameraPosition = CameraPosition.Builder().target(sydney).zoom(10F).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        })

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}// Required empty public constructor
