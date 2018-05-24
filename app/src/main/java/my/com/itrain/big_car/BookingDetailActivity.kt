package my.com.itrain.big_car

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_booking_detail.*
import org.json.JSONException
import org.json.JSONObject

class BookingDetailActivity : AppCompatActivity() {

    var bookingURL = "https://gentle-atoll-11837.herokuapp.com/api/bookinghistory/"
    var booking_id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)

        setSupportActionBar(toolbar_booking)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        booking_id = intent.getIntExtra("booking_id", 0)

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(applicationContext)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, bookingURL+booking_id, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val bookData = response.getJSONObject("data")

                    val userData = bookData.getJSONObject("users")
                    bookerName?.text = userData.getString("name")
                    bookerNumber?.text = userData.getString("phonenumber")

                    bookingName?.text = bookData.getString("booking_name")

                    val serviceData = bookData.getJSONObject("service")

                    progressDialog.dismiss()
                }catch (e : JSONException){
                    e.printStackTrace()
                    progressDialog.dismiss()
                }
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                        progressDialog.dismiss()
                    }
                }){
        }

        requestVolley.add(jsonObjectRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
