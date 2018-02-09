package my.com.itrain.big_car


import android.Manifest
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_trips_content.*
import javax.xml.transform.Templates


/**
 * A simple [Fragment] subclass.
 */
class TripsContentFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_trips_content, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(map:GoogleMap) {
        mMap = map

//        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//            ActivityCompat.requestPermissions(activity, arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),0)
//        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)



//        val sydney = LatLng(3.1577636,101.71186)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Malaysia").snippet("How are you?"))
//        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(15F).build()
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        mMap.setOnMapLoadedCallback(object : GoogleMap.OnMapLoadedCallback{
            override fun onMapLoaded() {
                val locations = ArrayList<LatLng>()
                locations.add(LatLng(3.1577636,101.71186))
                locations.add(LatLng(3.1614164,101.71887160000006))
                locations.add(LatLng(3.1537798,101.71322140000007))

                for (latlng in locations){
                    mMap.addMarker(MarkerOptions().position(latlng).title("Title can be anything"))
                }

                val builder = LatLngBounds.Builder()
                builder.include(locations.get(0)) //Taking Point A (First LatLng)
                builder.include(locations.get(locations.size - 1)) //Taking Point B (Second LatLng)
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                mMap.moveCamera(cu)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
            }
        })
    }

}// Required empty public constructor
