package my.com.itrain.big_car

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.maps.GoogleMap

/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment() {

    private lateinit var mMap:GoogleMap
    var VehicleType = arrayOf<String>("4 Seats", "10 Seats", "12 Seats", "16 Seats", "6 Seats")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_browse_content, container, false)

        val vehicle_type_spinner = rootView.findViewById<Spinner>(R.id.vehicle_spinner)
        var adapter = ArrayAdapter<String>(context, R.layout.vehicle_type, R.id.vehicle_text, VehicleType)
        vehicle_type_spinner.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}// Required empty public constructor
