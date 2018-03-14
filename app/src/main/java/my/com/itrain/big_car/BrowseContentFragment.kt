package my.com.itrain.big_car


import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DimenRes
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_browse_content.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment() {

    private lateinit var mMap:GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_content, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val placeAutoComplete = childFragmentManager.findFragmentById(R.id.place_autocomplete) as PlaceAutocompleteFragment
//        placeAutoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener{
//            override fun onPlaceSelected(p0: Place?) {
//                Toast.makeText(activity, "This Place Selected"+p0, Toast.LENGTH_LONG).show()
//            }
//
//            override fun onError(p0: Status?) {
//                Toast.makeText(activity,"Error"+p0, Toast.LENGTH_LONG).show()
//            }
//        })

//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

//    override fun onMapReady(map: GoogleMap) {
//        mMap = map
//    }


}// Required empty public constructor