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
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_attraction.*
import org.json.JSONException
import org.json.JSONObject

class AttractionActivity : AppCompatActivity() {

    var attractURL = "https://gentle-atoll-11837.herokuapp.com/api/attractiondetail/"
    var attractGalleryMaterial = ArrayList<String>()
    var attraction_id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attraction)

//        setSupportActionBar(toolbar_attraction)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        attraction_id = intent.getIntExtra("attraction_id", 0)

        val attractGallerAdapter = AttractGalleryAdapter(applicationContext, attractGalleryMaterial)
        attractGalleryViewPager.setAdapter(attractGallerAdapter)
        indicatorattractGallery.setViewPager(attractGalleryViewPager)
        attractGallerAdapter.registerDataSetObserver(indicatorattractGallery.getDataSetObserver())

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading")
        progressDialog.show()

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,attractURL+attraction_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val attractData = response.getJSONObject("data")
                    attractGalleryMaterial.add(attractData.getString("attraction_image"))
                    attractGallerAdapter.notifyDataSetChanged()
                    attractName?.text = attractData.getString("attraction_name")
                    attractDesc?.text = Html.fromHtml(attractData.getString("information"))
                    attractHistory?.text = Html.fromHtml(attractData.getString("history"))

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

class AttractGalleryAdapter(private val context: Context, private val tourGallery:ArrayList<String>):PagerAdapter() {

    var inflater: LayoutInflater = context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val view = inflater.inflate(R.layout.tour_gallery, container, false)
        val tourBannerImage = view.findViewById<ImageView>(R.id.tourImage)
        if (tourGallery.get(position) == ""){
            tourBannerImage.setImageResource(R.drawable.no_available)
        }else {
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