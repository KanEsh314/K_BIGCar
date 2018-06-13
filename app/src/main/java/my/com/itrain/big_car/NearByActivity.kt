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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_near_by.*
import org.json.JSONException
import org.json.JSONObject

class NearByActivity : AppCompatActivity() {

    var suggestURL = "https://gentle-atoll-11837.herokuapp.com/api/attractiontour/"
    var attractMaterial = ArrayList<JSONObject>()
    var attractURL = "https://gentle-atoll-11837.herokuapp.com/api/attractiondetail/"
    var attractGalleryMaterial = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_by)

        val attraction_id = intent.getIntExtra("attraction_id",0)

        val nattractGallerAdapter = NAttractGalleryAdapter(applicationContext, attractGalleryMaterial)
        nattractGalleryViewPager.setAdapter(nattractGallerAdapter)
        nindicatorattractGallery.setViewPager(nattractGalleryViewPager)
        nattractGallerAdapter.registerDataSetObserver(nindicatorattractGallery.getDataSetObserver())

        val attractAdapter = ExplorePopularContent(this, object: ExplorePopularContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, TourDetailActivity::class.java)
                try{
                    intent.putExtra("service_id", attractMaterial.get(position).getInt("service_id"))
                }catch (e :JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })
        val attractLayoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, true)
        recycleViewSuggest!!.layoutManager = attractLayoutManager
        recycleViewSuggest!!.itemAnimator = DefaultItemAnimator()
        recycleViewSuggest!!.adapter = attractAdapter

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()


        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,attractURL+attraction_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val attractData = response.getJSONObject("data")
                    attractGalleryMaterial.add(attractData.getString("attraction_image"))
                    nattractGallerAdapter.notifyDataSetChanged()
                    nattractName?.text = attractData.getString("attraction_name")
                    nattractDesc?.text = Html.fromHtml(attractData.getString("information"))
                    nattractHistory?.text = Html.fromHtml(attractData.getString("history"))

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

        var jsonObjectRequestSuggest = JsonObjectRequest(Request.Method.GET,suggestURL+attraction_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val attractData = response.getJSONArray("data")
                    if (attractData.length() == 0){
                        suggestionLayout.setVisibility(RelativeLayout.GONE)
                    } else {
                        for (i in 0 until attractData.length()){
                            attractMaterial.add(attractData.getJSONObject(i))
                            attractAdapter.addJsonObject(attractData.getJSONObject(i))
                            attractAdapter.notifyDataSetChanged()
                        }
                    }

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

        requestVolley.add(jsonObjectRequestSuggest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class AttractionAdapter(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<AttractionAdapter.ViewHolder>(){

    private val attraction = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var placeImg: ImageView
        var placeName: TextView

        init {
            placeImg = itemView.findViewById(R.id.placeImg)
            placeName = itemView.findViewById(R.id.placeName)
        }

        fun bind(position: Int, listener: AttractionAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_placecontent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        if (attraction.get(position).getString("grid_image") == ""){
            holder?.placeImg?.setImageResource(R.drawable.no_available  )
        }else{
            Picasso.with(context).load(attraction.get(position).getString("grid_image")).into(holder?.placeImg)
        }
        holder?.placeName?.text = attraction.get(position).getString("product_name")

        holder?.bind(position,listener)

    }

    override fun getItemCount(): Int {
        return attraction.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject: JSONObject) {
        attraction.add(jsonObject)
    }

}

class NAttractGalleryAdapter(private val context: Context, private val tourGallery:ArrayList<String>): PagerAdapter() {

    var inflater: LayoutInflater = context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val view = inflater.inflate(R.layout.tour_gallery, container, false)
        val tourBannerImage = view.findViewById<ImageView>(R.id.tourImage)
        if (tourGallery.get(position) == ""){
            tourBannerImage.setImageResource(R.drawable.no_available)
        } else {
            Picasso.with(context).load(tourGallery.get(position)).into(tourBannerImage)
        }
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
