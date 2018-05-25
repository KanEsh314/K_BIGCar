package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_booking_detail.*
import kotlinx.android.synthetic.main.post_review.*
import org.json.JSONException
import org.json.JSONObject

class BookingDetailActivity : AppCompatActivity() {

    var bookingURL = "https://gentle-atoll-11837.herokuapp.com/api/bookinghistory/"
    var commentURL = "http://gentle-atoll-11837.herokuapp.com/api/review/"
    var booking_id:Int = 0
    var service_id:Int = 0

    var CheckEditText:Boolean = false
    var commentHolder:String = ""
    var ratingHolder: String = ""

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

        postComment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                if(applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).contains("myToken")){
                    CheckEditTextIsEmptyOrNot()
                    if (CheckEditText){
                        commentPost()
                        Toast.makeText(applicationContext, "Thank You For Your Review", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                    }
                }else{
                    startActivity(Intent(applicationContext, StartActivity::class.java))
                }


            }
        })

        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, bookingURL+booking_id, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val bookData = response.getJSONObject("data")
                    service_id = bookData.getInt("service_id")

                    if (bookData.getString("status") == "5"){
                        commentZone.setVisibility(LinearLayout.VISIBLE)
                    }

                    bookerName?.text = bookData.getString("name")
                    bookerNumber?.text = bookData.getString("phonenumber")
                    bookingName?.text = bookData.getString("booking_name")
                    Picasso.with(applicationContext).load(bookData.getString("image")).into(booked_img)
                    booked_name?.text = bookData.getString("product_name")
                    booked_location?.text = bookData.getString("location")
                    booked_package_title?.text = bookData.getString("package_title")
                    booked_package_price?.text = bookData.getString("package_price")
                    booked_date?.text = bookData.getString("travel_date")
                    booked_time?.text = bookData.getString("travel_time")

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

    private fun CheckEditTextIsEmptyOrNot() {
        commentHolder = postText.text.toString()
        ratingHolder = postRating.rating.toString()

        if (TextUtils.isEmpty(commentHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }

    private fun commentPost() {
        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server")
        progressDialog.show()

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        val stringRequest = object : StringRequest(Request.Method.POST, commentURL+booking_id, object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                Log.d("Debug", response)
                progressDialog.dismiss()
                //Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog.dismiss()
                Log.d("Debug", error.toString())
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("user_comment", commentHolder)
                params.put("rating_id", ratingHolder)
                params.put("service_id", service_id.toString())
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(this)
        requestVolley.add(stringRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
