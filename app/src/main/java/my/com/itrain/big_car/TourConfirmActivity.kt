package my.com.itrain.big_car

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_tour_confirm.*
import kotlinx.android.synthetic.main.activity_tour_count.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TourConfirmActivity : AppCompatActivity() {

    var bookingURL = "http://gentle-atoll-11837.herokuapp.com/api/booking"
    var CheckEditText:Boolean = false

    var service_name : String= ""
    var service_package : String= ""
    var travel_date : String = ""
    var travel_day : String = ""
    var travel_time : String = ""
    var name_booking : String = ""
    var mobile_number : String = ""
    var nationality : String = ""
    var user_email : String = ""
    var user_id : String = ""
    var passenger_name : String = ""
    var ic_passport : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_confirm)

        setSupportActionBar(toolbarConfirm)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val service_name = intent.getIntExtra("service_id", 0)
        val travel_date = intent.getStringExtra("travel_date")

        Log.d("Service", service_name.toString())

        to_summary.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d("here","here")
                sendBooking()
//                CheckEditTextIsEmptyOrNot()
//                if (CheckEditText){
//                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
//
//                    try{
//
//                    }catch (e : JSONException){
//
//                    }
//                }else{
//                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
//                }
            }
        })

        //Title
//        selectTitle.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//            }
//        })
    }

    private fun sendBooking() {
//        val progressDialog = ProgressDialog(applicationContext, R.style.DialogTheme)
//        progressDialog.setMessage("Please Wait")
//        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, bookingURL, object : Response.Listener<String> {
            override fun onResponse(response: String?) {
                Log.d("Response", response)
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("Error", error.toString())
                    }

                }){
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("service_name", "11")
                params.put("service_package", "71")
                params.put("travel_day", "22")
                params.put("travel_time", "22")
                params.put("travel_date", "2017-01-11")
                params.put("booking_name", "TripWas")
                params.put("mobile_number", "01234567890")
                params.put("nationality", "11")
                params.put("email", "Testing@gmail.com")
                params.put("user_id", "101")
                var userArray:JSONArray = JSONArray()
                var newObj: JSONObject = JSONObject()
                newObj.put("passenger_name", "Wan Muz")
                newObj.put("ic_passport","98765")
                userArray.put(0, newObj)
                params.put("passengers", newObj.toString())
                Log.d("param",params.toString())
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(applicationContext)
        requestVolley.add(stringRequest)
    }

    private fun CheckEditTextIsEmptyOrNot() {

        service_name = name_service.text.toString()
        service_package = package_service.text.toString()
        travel_date = travel_day_date.text.toString()

        name_booking = booking_name.text.toString()
        mobile_number = phoneNumber.text.toString()
        nationality = selectOrigin.toString()
        user_email = email.text.toString()

        passenger_name = name_passenger.text.toString()
        ic_passport = ic_number.text.toString()

        if (TextUtils.isEmpty(service_name) || TextUtils.isEmpty(service_package) || TextUtils.isEmpty(travel_date) || TextUtils.isEmpty(name_booking) || TextUtils.isEmpty(mobile_number) || TextUtils.isEmpty(nationality) || TextUtils.isEmpty(user_email) || TextUtils.isEmpty(passenger_name) || TextUtils.isEmpty(ic_passport)){
            CheckEditText = false
        }else{
            CheckEditText = true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
