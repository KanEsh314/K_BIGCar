package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.MenuItemCompat
import android.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_tour_detail.*
import kotlinx.android.synthetic.main.post_review.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TourDetailActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"
    var commentURL = "http://gentle-atoll-11837.herokuapp.com/api/review/"
    var favoriteURL = "http://gentle-atoll-11837.herokuapp.com/api/favoritetour"
    private val tourMaterial = ArrayList<JSONObject>()
    var CheckEditText:Boolean = false
    var service_id:Int = 0
    var commentHolder:String = ""
    var ratingHolder: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        service_id = intent.getIntExtra("service_id", 0)

        favourite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val progressDialog = ProgressDialog(applicationContext, R.style.DialogTheme)
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog.setTitle("Please Wait")
                progressDialog.setMessage("Loading")
                progressDialog.show()

                val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
                val stringRequest = object : StringRequest(Request.Method.POST, favoriteURL, object : Response.Listener<String>{
                    override fun onResponse(response: String?) {
                        Log.d("Debug", response)
                        progressDialog.dismiss()
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
                        params.put("service_id", service_id.toString())
                        return params
                    }
                }

                val requestVolley = Volley.newRequestQueue(applicationContext)
                requestVolley.add(stringRequest)


            }

        })

        open_share.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent()
                try {
                    intent.setAction(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, "http://www.bigtreetours.com/")
                    intent.setType("text/plain")
                }catch (e : Exception){
                    e.printStackTrace()
                }
                startActivity(intent)
            }

        })

        postcomment.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    commentPost()
                    Toast.makeText(applicationContext, "Thank You For Your Review", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })

        val checkDates = findViewById<View>(R.id.add_to_cart_btn)
        checkDates.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@TourDetailActivity, TourDatesActivity::class.java)
                try {
                    intent.putExtra("service_id", service_id)
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading")
        progressDialog.show()

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val reviewAdapter = ReviewContentAdapter(this)
        val reviewLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycleTourReview!!.layoutManager = reviewLayoutManager
        recycleTourReview!!.itemAnimator = DefaultItemAnimator()
        recycleTourReview!!.adapter = reviewAdapter

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,tourURL+service_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val tourData = response.getJSONObject("data")
                    tourMaterial.add(tourData)

                    tourName.text = tourData.getString("product_name")
                    tourDesc.text = Html.fromHtml(tourData.getString("product_desc"))
                    tourExplanation.text = Html.fromHtml(tourData.getString("highlight"))

                    val tourGallery = tourData.getJSONArray("tour_gallery")
                    for (j in 0 until tourGallery.length()){
                        Picasso.with(applicationContext).load(tourGallery.getJSONObject(j).getString("image")).into(tourImage)
                    }

                    val tourReviewData = tourData.getJSONArray("reviews")
                    for (i in 0 until tourReviewData.length()){
                        reviewAdapter.addJsonObject(tourReviewData.getJSONObject(i))
                    }

                    reviewAdapter.notifyDataSetChanged()
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

    private fun CheckEditTextIsEmptyOrNot() {
        commentHolder = comment.text.toString()
        ratingHolder = ratingcomment.rating.toString()

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
        val stringRequest = object : StringRequest(Request.Method.POST, commentURL+service_id, object : Response.Listener<String>{
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

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
