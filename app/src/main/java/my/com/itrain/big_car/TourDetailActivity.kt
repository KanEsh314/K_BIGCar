package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
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
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tour_detail.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TourDetailActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"

    var favoriteURL = "http://gentle-atoll-11837.herokuapp.com/api/favoritetour"
    private val tourMaterial = ArrayList<JSONObject>()
    private val tourGalleryMaterial = ArrayList<String>()
    private val tourHighlightMaterial = ArrayList<JSONObject>()
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

                    val jsonBody = JSONObject()
                    try
                    {
                        jsonBody.put("service_id", service_id.toString())
                    }
                    catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    val stringRequest = object : JsonObjectRequest(Request.Method.POST, favoriteURL, jsonBody,object : Response.Listener<JSONObject>{
                        override fun onResponse(response: JSONObject) {
                            if (response.getString("status") == "true"){
                                favourite.setBackgroundResource(R.mipmap.ic_action_like)
                            } else if (response.getString("status") == "false"){
                                favourite.setBackgroundResource(R.mipmap.ic_action_unlike)
                            }
                        }
                    }, object : Response.ErrorListener{
                        override fun onErrorResponse(error: VolleyError?) {
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

                    val requestVolley = Volley.newRequestQueue(applicationContext)
                    requestVolley.add(stringRequest)

                }else{
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
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
                if (applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","") == ""){
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                } else {
                    startActivity(intent)
                }
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

        val highlightAdapter = HighlightAdapter(this)
        val hightlightLayoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        recycleViewHighlight!!.layoutManager = hightlightLayoutManager
        recycleViewHighlight!!.itemAnimator = DefaultItemAnimator()
        recycleViewHighlight!!.adapter = highlightAdapter

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,tourURL+service_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val tourData = response.getJSONObject("data")
                    tourMaterial.add(tourData)

                    val tourGallery = tourData.getJSONArray("tour_gallery")
                    tourGalleryMaterial.add(tourData.getString("grid_image"))
                    for (i in 0 until tourGallery.length()){
                        tourGalleryMaterial.add(tourGallery.getJSONObject(i).getString("image"))
                    }
                    tourGallerAdapter.notifyDataSetChanged()

                    product_name = tourData.getString("product_name")

                    tourName?.text = tourData.getString("product_name")
                    tourDesc?.text = Html.fromHtml(tourData.getString("product_desc"))
                    tourOverview?.text = Html.fromHtml(tourData.getString("overview"))

                    totalReviews.text = tourData.getString("total_review")
                    collectRating.rating = tourData.getString("total_rating").toFloat()
                    collectRatingText.text = tourData.getString("total_rating")

                    val tourHighlight = tourData.getJSONArray("highlight")
                    for (i in 0 until tourHighlight.length()){
                        highlightAdapter.addJsonObject(tourHighlight.getJSONObject(i))
                    }
                    highlightAdapter.notifyDataSetChanged()

                    if (tourData.getString("favorite") == "true"){
                        favourite.setBackgroundResource(R.mipmap.ic_action_like)
                    } else if (tourData.getString("favorite") == "false"){
                        favourite.setBackgroundResource(R.mipmap.ic_action_unlike)
                    } else {
                        favourite.setBackgroundResource(R.mipmap.ic_action_unlike)
                    }

                    val tourService = tourData.getJSONArray("service_information")
                    for (i in 0 until tourService.length()){
                        activityAdapter.addJsonObject(tourService.getJSONObject(i))
                    }
                    activityAdapter.notifyDataSetChanged()

                    val tourActivity = tourData.getJSONArray("activity_information")
                    for (i in 0 until tourActivity.length()){
                        activityAdapter.addJsonObject(tourActivity.getJSONObject(i))
                    }
                    activityAdapter.notifyDataSetChanged()

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
                }){
                    @Throws(AuthFailureError::class)
                    override fun getHeaders():Map<String,String>{
                        val headers = HashMap<String, String>()
                        headers.put("Authorization", "Bearer "+sharedPreferences)
                    return headers
                }
        }

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

class HighlightAdapter(private val context: Context):RecyclerView.Adapter<HighlightAdapter.ViewHolder>() {

    private val highlight = ArrayList<JSONObject>()

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var highlightImg: ImageView
        var highlightText: TextView

        init {
            highlightImg = itemView.findViewById(R.id.highImg)
            highlightText = itemView.findViewById(R.id.highText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tour_highlight, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (highlight.get(position).getString("attraction_image") == ""){
            holder?.highlightImg?.setImageResource(R.drawable.no_available)
        }else {
            Picasso.with(context).load(highlight.get(position).getString("attraction_image")).into(holder?.highlightImg)
        }
        holder?.highlightText?.text = highlight.get(position).getString("attraction_name")
    }

    override fun getItemCount(): Int {
        return highlight.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        highlight.add(jsonObject)
    }

}

class TourGalleryAdapter(private val context: Context, private val tourGallery:ArrayList<String>):PagerAdapter() {

    var inflater: LayoutInflater = context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val view = inflater.inflate(R.layout.tour_gallery, container, false)
        val tourBannerImage = view.findViewById<ImageView>(R.id.tourImage)
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
