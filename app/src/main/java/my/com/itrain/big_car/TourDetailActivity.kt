package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tour_detail.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TourDetailActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"
    private val tourMaterial = java.util.ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val tourService_id = intent.getIntExtra("serviceid",0)

        open_share.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(applicationContext,"Item Shared", Toast.LENGTH_LONG).show()
            }

        })

        val checkDates = findViewById<View>(R.id.add_to_cart_btn)
        checkDates.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@TourDetailActivity, TourDatesActivity::class.java)
                try {
                    intent.putExtra("serviceid", tourService_id)
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val reviewAdapter = ReviewContentAdapter(this)
        val reviewLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourReview!!.layoutManager = reviewLayoutManager
        recycleTourReview!!.itemAnimator = DefaultItemAnimator()
        recycleTourReview!!.adapter = reviewAdapter

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,tourURL+tourService_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val tourData = response.getJSONObject("data")
                    tourMaterial.add(tourData)

                    val tourGallery = tourData.getJSONArray("tour_gallery")
                    for (j in 0 until tourGallery.length()){
                        Picasso.with(applicationContext).load(tourGallery.getJSONObject(j).getString("image")).into(tourImage)
                    }

                    tourName.text = tourData.getString("product_name")
                    tourDesc.text = Html.fromHtml(tourData.getString("product_desc"))
                    tourExplanation.text = Html.fromHtml(tourData.getString("highlight"))

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
