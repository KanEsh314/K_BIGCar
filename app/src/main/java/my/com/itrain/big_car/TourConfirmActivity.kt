package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_tour_confirm.*
import kotlinx.android.synthetic.main.activity_tour_count.*
import my.com.itrain.big_car.R.id.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TourConfirmActivity : AppCompatActivity() {

    var countryURL = "https://gentle-atoll-11837.herokuapp.com/api/countries"
    var bookingURL = "http://gentle-atoll-11837.herokuapp.com/api/booking"
    val countryMaterial = ArrayList<JSONObject>()
    var CheckEditText:Boolean = false

    var name_booking : String = ""
    var mobile_number : String = ""
    var nationality : String = ""
    var user_email : String = ""
    var passenger_name : String = ""
    var ic_passport : String = ""
    var tour_name: String = ""
    var package_name: String = ""
    var package_pax: String = ""
    var travel_time: String = ""
    var travel_date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_confirm)

        setSupportActionBar(toolbarConfirm)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        tour_name = intent.getStringExtra("tour_name")
        name_tour?.text = tour_name
        package_name = intent.getStringExtra("package_name")
        name_package?.text = package_name
        package_pax = intent.getStringExtra("package_pax")
        package_service?.text = package_pax
        travel_date = intent.getStringExtra("travel_date")
        date_travel?.text = travel_date
        travel_time = intent.getStringExtra("travel_time")
        time_travel?.text = travel_time

        val customSpinnerAdapter = CustomSpinnerAdapter(this)
        selectOrigin.setAdapter(customSpinnerAdapter)
        selectOrigin.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                nationality = countryMaterial.get(position).getString("id")
            }
        })

        to_summary.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d("here","here")
                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    sendBooking()
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })

        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()


        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,countryURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val countryData = response.getJSONArray("data")

                    for (i in 0 until countryData.length()){
                        countryMaterial.add(countryData.getJSONObject(i))
                        customSpinnerAdapter.addJsonObject(countryData.getJSONObject(i))
                    }
                    customSpinnerAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequest)
    }

    private fun sendBooking() {

        val postBooking = JSONObject()
        try {
            val service_id = intent.getIntExtra("service_id", 0)
            val package_id = intent.getStringExtra("package_id")
            val travel_date = intent.getStringExtra("travel_date")
            val travel_time_id = intent.getStringExtra("travel_time_id")
            val travel_day_id = intent.getStringExtra("travel_day_id")

            var userArray = JSONArray()
            val newObj = JSONObject()
            try{
                newObj.put("passenger_name", passenger_name)
                newObj.putOpt("ic_passport",ic_passport)
            }catch (e : JSONException){
                e.printStackTrace()
            }
            userArray.put(newObj)
            Log.d("Array", userArray.toString())

            val bookingObj = JSONObject()
            bookingObj.put("service_name", service_id.toString())
            bookingObj.put("service_package", package_id.toString())
            bookingObj.put("travel_day", travel_day_id)
            bookingObj.put("travel_time", travel_time_id)
            bookingObj.put("travel_date", travel_date)
            bookingObj.put("booking_name", name_booking)
            bookingObj.put("mobile_number", mobile_number)
            bookingObj.put("nationality", "11")
            bookingObj.put("email", user_email)
            bookingObj.put("passengers", userArray.toString())
            Log.d("Booking", bookingObj.toString())

            postBooking.put("booking", bookingObj.toString())
            Log.d("Debug",postBooking.toString())

        }catch (e : JSONException){
            e.printStackTrace()
            Log.d("Error", e.toString())
        }

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        val jsonRequest = object :JsonObjectRequest(Request.Method.POST, bookingURL, postBooking, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject?) {
                Log.d("Debug", response.toString())
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
            val intent = Intent(this@TourConfirmActivity, TourSummaryActivity::class.java)
            try {
                intent.putExtra("tour_name", tour_name)
                intent.putExtra("package_name", package_name)
                intent.putExtra("package_pax", package_pax)
                intent.putExtra("travel_date", travel_date)
                intent.putExtra("travel_time", travel_time)
                intent.putExtra("booking_name", name_booking)
                intent.putExtra("mobile_number", mobile_number)
                intent.putExtra("nationality", nationality)
                intent.putExtra("user_email", user_email)
                intent.putExtra("passenger_name", passenger_name)
                intent.putExtra("ic_passport", ic_passport)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            startActivity(intent)
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        error.printStackTrace()
                        Log.d("Error", error.toString())
                    }
                }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                headers.put("Content-Type", "application/json; charset=utf-8")
                return headers
            }
        }
        val requestVolleyBooking = Volley.newRequestQueue(applicationContext)
        requestVolleyBooking.add(jsonRequest)
    }

    private fun CheckEditTextIsEmptyOrNot() {

        name_booking = booking_name.text.toString()
        mobile_number = phoneNumber.text.toString()
        nationality = selectOrigin.toString()
        user_email = email.text.toString()
        passenger_name = name_passenger.text.toString()
        ic_passport = ic_number.text.toString()

        if (TextUtils.isEmpty(name_booking) || TextUtils.isEmpty(mobile_number) || TextUtils.isEmpty(nationality) || TextUtils.isEmpty(user_email) || TextUtils.isEmpty(passenger_name) || TextUtils.isEmpty(ic_passport)){
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

class CustomSpinnerAdapter(private val context: Context):BaseAdapter() {

    private val nationality = ArrayList<JSONObject>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(context)
        textView.gravity = Gravity.LEFT
        textView.setPadding(16, 16, 16, 16)
        textView.textSize = 14F
        textView.text = nationality.get(position).getString("name")
        textView.setTextColor(Color.parseColor("#000000"))
        return textView
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return nationality.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        nationality.add(jsonObject)
    }

}
