package my.com.itrain.big_car

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_tour_confirm.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import android.widget.LinearLayout
import java.text.DateFormatSymbols
import com.android.volley.DefaultRetryPolicy
import com.paypal.android.sdk.payments.*
import com.paypal.android.sdk.payments.PaymentActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account.*
import my.com.itrain.big_car.R.id.*
import java.math.BigDecimal


class TourConfirmActivity : AppCompatActivity() {

    var countryURL = "https://gentle-atoll-11837.herokuapp.com/api/countries"
    var paymentURL = "https://gentle-atoll-11837.herokuapp.com/api/paymentmethods"
    var bookingURL = "https://gentle-atoll-11837.herokuapp.com/api/booking"
    var userURL = "https://gentle-atoll-11837.herokuapp.com/api/user"

    val countryMaterial = ArrayList<JSONObject>()
    var CheckEditText:Boolean = false

    private var PAYPAL_REQUEST_CODE = 7171
    private var paypalConfiguration = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AT8fbwr44fyzAL6KLB0nY3AujMfflCmVMI7ZCQ5g-Bu6LMyqeqYMPX4lZJqvx4PX_d4xv1QCzBTG6nO5")

    var booking_first : String = ""
    var booking_last : String = ""
    var mobile_number : String = ""
    var nationality_id : String = ""
    var nationality : String = ""
    var user_email : String = ""
    var tour_name: String = ""
    var package_pax: String = ""
    var package_title: String = ""
    var package_price:String = ""
    var travel_time: String = ""
    var travel_date: String = ""
    var remark: String = ""

    var paymentDetail: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_confirm)

        setSupportActionBar(toolbarConfirm)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val intentServices = Intent(applicationContext, PayPalService::class.java)
        intentServices.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration)
        startService(intentServices)

        tour_name = intent.getStringExtra("tour_name")
        name_tour?.text = tour_name
        package_title = intent.getStringExtra("package_title")
        name_package?.text = package_title

        val onDateYear = intent.getIntExtra("selectedYear",0)
        val onDateMonth = intent.getIntExtra("selectedMonth", 0)
        val onDateDay = intent.getIntExtra("selectedDay", 0)
        travel_date = onDateDay.toString() + " " + DateFormatSymbols().getMonths()[onDateMonth - 1] + " " + onDateYear.toString()
        date_travel?.text = onDateDay.toString() + " " + DateFormatSymbols().getMonths()[onDateMonth - 1] + " " + onDateYear.toString()

        travel_time = intent.getStringExtra("travel_time")
        time_travel?.text = travel_time
        package_pax = intent.getStringExtra("package_pax")
        package_service?.text = package_pax
        package_price = intent.getStringExtra("package_price")
        totalPrice?.text = "RM"+package_price

        val customSpinnerAdapter = CustomSpinnerAdapter(this)
        selectOrigin.setAdapter(customSpinnerAdapter)
        selectOrigin.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do Nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectOrigin.isSelected = countryMaterial.get(position).getString("name") == "Malaysia"
                nationality_id = countryMaterial.get(position).getString("id")
                nationality = countryMaterial.get(position).getString("name")
            }
        })

        val tourPaymentAdapter = PaymentAdapter(this)
        val tourPaymentLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleViewPaymentType!!.layoutManager = tourPaymentLayoutManager
        recycleViewPaymentType!!.itemAnimator = DefaultItemAnimator()
        recycleViewPaymentType!!.adapter = tourPaymentAdapter

        to_summary.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Log.d("here","here")
                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    if (tourPaymentAdapter.getSelectedItem().getString("method") == "PayPal"){
                        processPayment()
                    } else if (tourPaymentAdapter.getSelectedItem().getString("method") == "Cash"){
                        sendBooking()
                    } else if (tourPaymentAdapter.getSelectedItem().getString("method") == "Debit Card"){

                    } else if (tourPaymentAdapter.getSelectedItem().getString("method") == "Credit Card"){

                    } else if (tourPaymentAdapter.getSelectedItem().length() == 0){
                        Toast.makeText(applicationContext, "Choose Payment Method", Toast.LENGTH_LONG).show()
                    }

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
                        selectOrigin.prompt = "Malaysia"
                        countryMaterial.add(countryData.getJSONObject(i))
                        customSpinnerAdapter.addJsonObject(countryData.getJSONObject(i))
                    }
                    customSpinnerAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }catch (e : JSONException){
                    progressDialog.dismiss()
                    e.printStackTrace()
                }
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        progressDialog.dismiss()
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequest)

        var jsonObjectRequestPayment = JsonObjectRequest(Request.Method.GET,paymentURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val paymentData = response.getJSONArray("data")

                    for (i in 0 until paymentData.length()){
                        tourPaymentAdapter.addJsonObject(paymentData.getJSONObject(i))
                    }
                    tourPaymentAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }catch (e : JSONException){
                    progressDialog.dismiss()
                    e.printStackTrace()
                }
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        progressDialog.dismiss()
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequestPayment)

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                booking_first_tname?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("first_name"))
                booking_last_name?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("last_name"))
                phoneNumber?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("phonenumber"))
                email?.text = Editable.Factory.getInstance().newEditable(userInfo.getString("email"))
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("Debug", error.toString())
            }

        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
        }

        requestVolley.add(jsonRequest)
    }

        private fun sendBooking() {

            val progressDialog = ProgressDialog(this, R.style.DialogTheme)
            progressDialog.setCancelable(false)
            progressDialog.isIndeterminate=true
            progressDialog.show()

            val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
            val stringRequest = object : StringRequest(Request.Method.POST, bookingURL, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    val intent = Intent(applicationContext, TourSummaryActivity::class.java)
                    try {
                        intent.putExtra("tour_name", tour_name)
                        intent.putExtra("package_title", package_title)
                        intent.putExtra("package_pax", package_pax)
                        intent.putExtra("travel_date", travel_date)
                        intent.putExtra("travel_time", travel_time)
                        intent.putExtra("first_name", booking_first)
                        intent.putExtra("last_name", booking_last)
                        intent.putExtra("mobile_number", mobile_number)
                        intent.putExtra("nationality", nationality)
                        intent.putExtra("user_email", user_email)
                        intent.putExtra("remark", remark)
                        intent.putExtra("package_price", package_price)
                        var jsonObj = JSONObject(paymentDetail)
                        intent.putExtra("paymentID", jsonObj.getJSONObject("response").getString("id"))
                        intent.putExtra("paymentState", jsonObj.getJSONObject("response").getString("state"))

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    startActivity(intent)
                    Log.d("Debug", response)
                }
            },
                    object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            Log.d("Error", error.toString())
                            progressDialog.dismiss()
                        }

                    }){
                @Throws(AuthFailureError::class)
                override fun getHeaders():Map<String,String>{
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer "+sharedPreferences)
                    headers.put("Content-Type", "application/x-www-form-urlencoded")
                    return headers
                }
                override fun getParams():Map<String, String> {
                    val params = HashMap<String, String>()

                    val service_id = intent.getIntExtra("service_id", 0).toString()
                    val package_id = intent.getStringExtra("package_id")
                    val travel_date = intent.getStringExtra("travel_date")
                    val travel_time_id = intent.getStringExtra("travel_time_id")
                    val travel_day_id = intent.getStringExtra("travel_day_id")

                    params.put("service_name", service_id)
                    params.put("service_package", package_id)
                    params.put("travel_date", travel_date)
                    params.put("travel_day", travel_day_id)
                    params.put("travel_time", travel_time_id)
                    params.put("remark", remark)
                    params.put("first_name", booking_first)
                    params.put("last_name", booking_last)
                    params.put("mobile_number", mobile_number)
                    params.put("email", user_email)
                    params.put("nationality",  nationality_id)
                    return params
                }
            }

            val requestVolley = Volley.newRequestQueue(applicationContext)
            stringRequest.retryPolicy = DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            requestVolley.add(stringRequest)
    }

    private fun processPayment() {
        val payPalPayment = PayPalPayment(package_price.toBigDecimal(), "USD", tour_name, PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(applicationContext, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    private fun CheckEditTextIsEmptyOrNot() {

        booking_first = booking_first_tname.text.toString()
        booking_last = booking_last_name.text.toString()
        mobile_number = phoneNumber.text.toString()
        user_email = email.text.toString()
        remark = remarks.text.toString()

        if (TextUtils.isEmpty(booking_first) || TextUtils.isEmpty(booking_last) || TextUtils.isEmpty(mobile_number) || TextUtils.isEmpty(user_email)){
            CheckEditText = false
        }else{
            CheckEditText = true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){

                val paymentConfirmation = data.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (paymentConfirmation != null){
                    paymentDetail = paymentConfirmation.toJSONObject().toString(4)
                    sendBooking()
                }
            } else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(applicationContext, "This Payment Has Been Cancelled", Toast.LENGTH_LONG).show()
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(applicationContext, "Invalid", Toast.LENGTH_LONG).show()
            } else {
                Log.d("Debug", "Payment Problem")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        stopService(Intent(applicationContext, PayPalService::class.java))
        super.onDestroy()
    }
}

class PaymentAdapter(private val context: Context): RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    private val paymentType = ArrayList<JSONObject>()
    private var selectedPosition = -1

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var paymentText: TextView
        var paymentMethod: RadioButton

        init {
            paymentText = itemView.findViewById(R.id.tourSelectText)
            paymentMethod = itemView.findViewById(R.id.tourSelectTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tourselect_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.paymentText?.text = paymentType.get(position).getString("method")
        holder?.paymentMethod?.setChecked(position === selectedPosition)
        holder?.paymentMethod?.setTag(position)
        holder?.paymentMethod?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                itemCheckChanged(v)
            }
        })
    }

    private fun itemCheckChanged(v: View?) {
        selectedPosition = v?.getTag() as Int
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return paymentType.size
    }

    fun getSelectedItem():JSONObject {
        if (selectedPosition !== -1)
        {
            return paymentType.get(selectedPosition)
        }
        return JSONObject()
    }

    fun addJsonObject(jsonObject: JSONObject) {
        paymentType.add(jsonObject)
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
