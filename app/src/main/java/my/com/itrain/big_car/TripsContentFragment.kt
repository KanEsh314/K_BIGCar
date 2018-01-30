package my.com.itrain.big_car


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * A simple [Fragment] subclass.
 */
class TripsContentFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips_content, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap?) {
       mMap = map as GoogleMap;

        val malaysia = LatLng(3.519863, 101.538116)
        mMap.addMarker(MarkerOptions().position(malaysia).title("This Is Malaysia..."))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(malaysia))


    }
}// Required empty public constructor
