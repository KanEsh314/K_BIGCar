package my.com.itrain.big_car

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_pick.*

class PickActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick)

        val autocompleteFragment  = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status?) {
                Log.d("Debug", p0?.toString())
            }

            override fun onPlaceSelected(place: Place) {
                val intent = Intent()
                intent.putExtra("selectedPickName", place.name)
                intent.putExtra("selectedPickLat", place.latLng.latitude)
                intent.putExtra("selectedPickLng", place.latLng.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}
