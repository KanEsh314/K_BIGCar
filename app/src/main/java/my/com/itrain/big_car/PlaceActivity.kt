package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener

class PlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        val autoComplete = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autoComplete.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status?) {
                //Log.d("Debug", p0.toString())
            }

            override fun onPlaceSelected(p0: Place?) {
                Log.d("Debug", p0.toString())
                Toast.makeText(applicationContext, p0.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}
