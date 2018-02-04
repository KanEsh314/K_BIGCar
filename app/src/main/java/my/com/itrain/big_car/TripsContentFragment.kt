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
        System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        val sydney = LatLng(4.210484, 101.97576600000002);
        mMap.addMarker(MarkerOptions().position(sydney).title("Malaysia"))
        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(8F).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}// Required empty public constructor
