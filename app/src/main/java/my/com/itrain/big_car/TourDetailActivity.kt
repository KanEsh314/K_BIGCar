package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.MenuItemCompat
import android.app.AlertDialog
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tour_detail.*
import kotlinx.android.synthetic.main.fragment_explore_content.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class TourDetailActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"

    var favoriteURL = "http://gentle-atoll-11837.herokuapp.com/api/favoritetour"
    private val tourMaterial = ArrayList<JSONObject>()
    private val tourGalleryMaterial = ArrayList<String>()
    var service_id:Int = 0
    var product_name:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val tourGallerAdapter = TourGalleryAdapter(applicationContext, tourGalleryMaterial)
        tourGalleryViewPager.setAdapter(tourGallerAdapter)
        indicatorGallery.setViewPager(tourGalleryViewPager)
        tourGallerAdapter.registerDataSetObserver(indicatorGallery.getDataSetObserver())

        service_id = intent.getIntExtra("service_id", 0)
        Log.d("Debug",service_id.toString())

        favourite.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
                if(applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).contains("myToken")){

                    val stringRequest = object : StringRequest(Request.Method.POST, favoriteURL, object : Response.Listener<String>{
                        override fun onResponse(response: String?) {
                            Log.d("Debug", response)
                            Toast.makeText(applicationContext, "Successfully Added", Toast.LENGTH_LONG).show()
//                            progressDialog.dismiss()
                        }
                    }, object : Response.ErrorListener{
                        override fun onErrorResponse(error: VolleyError?) {
//                            progressDialog.dismiss()
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

                }else{
                    startActivity(Intent(applicationContext, StartActivity::class.java))
                }
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

        val checkDates = findViewById<View>(R.id.add_to_cart_btn)
        checkDates.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@TourDetailActivity, TourDatesActivity::class.java)
                try {
                    intent.putExtra("service_id", service_id)
                    intent.putExtra("product_name", product_name)
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

        val activityAdapter = ActivityInfoAdapter(this)
        val activityLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycleViewActivityInfo!!.layoutManager = activityLayoutManager
        recycleViewActivityInfo!!.itemAnimator = DefaultItemAnimator()
        recycleViewActivityInfo!!.adapter = activityAdapter

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

                    product_name = tourData.getString("product_name")

                    tourName?.text = tourData.getString("product_name")
                    tourDesc?.text = Html.fromHtml(tourData.getString("product_desc"))
                    tourOverview?.text = Html.fromHtml(tourData.getString("overview"))
                    tourHighlight?.text = Html.fromHtml(tourData.getString("highlight"))

                    totalReviews.text = tourData.getString("total_review")
                    collectRating.rating = tourData.getString("total_rating").toFloat()
                    collectRatingText.text = tourData.getString("total_rating")

                    val tourService = tourData.getJSONArray("service_information")
                    for (i in 0 until tourService.length()){
                        activityAdapter.addJsonObject(tourService.getJSONObject(i))
                    }

                    val tourActivity = tourData.getJSONArray("activity_information")
                    for (i in 0 until tourActivity.length()){
                        activityAdapter.addJsonObject(tourActivity.getJSONObject(i))
                    }
                    activityAdapter.notifyDataSetChanged()

                    val tourGallery = tourData.getJSONArray("tour_gallery")
                    tourGalleryMaterial.add(tourData.getString("grid_image"))
                    for (i in 0 until tourGallery.length()){
                        tourGalleryMaterial.add(tourGallery.getJSONObject(i).getString("image"))
                    }
                    tourGallerAdapter.notifyDataSetChanged()

                    val tourReviewData = tourData.getJSONArray("reviews")
                    for (i in 0 until tourReviewData.length()){
                        reviewAdapter.addJsonObject(tourReviewData.getJSONObject(i))
                    }
                    reviewAdapter.notifyDataSetChanged()
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

class TourGalleryAdapter(private val context: Context, private val tourGallery: ArrayList<String>):PagerAdapter() {

    var inflater: LayoutInflater = context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val view = inflater.inflate(R.layout.tour_gallery, container, false)
        val tourBannerImage = view.findViewById<ImageView>(R.id.tourImage)
        if (tourGallery.get(position) == ""){
            tourBannerImage.setImageResource(R.drawable.no_available)
        }
        Picasso.with(context).load(tourGallery.get(position)).into(tourBannerImage)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        //super.destroyItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return tourGallery.size
    }
}
