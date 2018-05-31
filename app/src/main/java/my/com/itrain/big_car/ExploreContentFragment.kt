package my.com.itrain.big_car


import android.app.Activity
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.banner_slider.*
import kotlinx.android.synthetic.main.fragment_explore_content.*
import my.com.itrain.big_car.R.id.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    var toursURL = "https://gentle-atoll-11837.herokuapp.com/api/tours"
    var popularsURL = "https://gentle-atoll-11837.herokuapp.com/api/populartour"
    var categoriesURL = "http://gentle-atoll-11837.herokuapp.com/api/categories"
    var bannerURl = "https://gentle-atoll-11837.herokuapp.com/api/banners"
    private val toursMaterial = ArrayList<JSONObject>()
    private val bannerMaterial = ArrayList<JSONObject>()
    private val categoriesMaterial = ArrayList<JSONObject>()
    private val popularMaterial = ArrayList<JSONObject>()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore_content, container, false)

        var newHeight = (Resources.getSystem().displayMetrics.heightPixels)/2
        var bannerViewPager = view.findViewById<View>(R.id.bannerViewPager)
        bannerViewPager.requestLayout()
        bannerViewPager.layoutParams.height = newHeight

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bannerAdapter = BannerAdapter(activity, bannerMaterial)
        bannerViewPager.setAdapter(bannerAdapter)

        //Check Data Exist
        Handler().postDelayed({

            if (toursMaterial.size == 0 && popularMaterial.size == 0 && categoriesMaterial.size == 0 && bannerMaterial.size == 0){
                Log.d("Debug", toursMaterial.size.toString()+" "+popularMaterial.size.toString()+" "+categoriesMaterial.size.toString()+" "+bannerMaterial.size.toString())
                AlertDialog.Builder(activity, R.style.DialogTheme)
                    .setCancelable(false)
                    .setTitle("Internet Error")
                    .setMessage("Please Connect To The Nearest WiFi")
                    .setPositiveButton("Okay", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            activity.finish()
                        }
                    })
                    .create()
                    .show()
            }

        }, 2500)

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(activity)

        val popularAdapter = ExploreContentAdapter(activity, object: ExploreContentAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, TourDetailActivity::class.java)
                try {
                    intent.putExtra("service_id", popularMaterial.get(position).getInt("service_id"))
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }

        })
        val popularLayoutManager = GridLayoutManager(this.activity,2)
        recycleViewActivities!!.layoutManager = popularLayoutManager
        recycleViewActivities!!.itemAnimator = DefaultItemAnimator()
        recycleViewActivities!!.adapter = popularAdapter



        val destinationAdapter = ExplorePlaceContentAdapter(activity ,object: ExplorePlaceContentAdapter.OnItemClickListener{
            override fun onItemClick(position:Int){
                val intent = Intent(activity, TourDetailActivity::class.java)
                try {
                    intent.putExtra("service_id", toursMaterial.get(position).getInt("service_id"))
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })
        val destinationLayoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL,false)
        recycleViewDestination!!.layoutManager = destinationLayoutManager
        recycleViewDestination!!.itemAnimator = DefaultItemAnimator()
        recycleViewDestination!!.adapter = destinationAdapter

        val categoryAdapter = CategoryAdapter(activity, object : CategoryAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, CategoryActivity::class.java)
                try {
                    intent.putExtra("servicecatid", categoriesMaterial.get(position).getInt("servicecat_id"))
                    intent.putExtra("categoryTitle", categoriesMaterial.get(position).getString("servicecat_name"))
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }

        })
        val categoryLayoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        recycleViewCategory!!.layoutManager = categoryLayoutManager
        recycleViewCategory!!.itemAnimator = DefaultItemAnimator()
        recycleViewCategory!!.adapter = categoryAdapter

        //VOLLEY
        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading")
        progressDialog.show()

        var jsonObjectRequestTour = JsonObjectRequest(Request.Method.GET, toursURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val toursData = response.getJSONArray("data")

                for (i in 0 until toursData.length()){
                    destinationAdapter.addJsonObject(toursData.getJSONObject(i))
                    toursMaterial.add(toursData.getJSONObject(i))
                }
                destinationAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            } catch (e : JSONException){
                e.printStackTrace()
                progressDialog.dismiss()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                        progressDialog.dismiss()
                    }
                })

        requestVolley.add(jsonObjectRequestTour)

        var jsonObjectRequestPopular = JsonObjectRequest(Request.Method.GET, popularsURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val toursData = response.getJSONArray("data")

                for (i in 0 until toursData.length()){
                    popularAdapter.addJsonObject(toursData.getJSONObject(i))
                    popularMaterial.add(toursData.getJSONObject(i))
                }
                popularAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            } catch (e : JSONException){
                e.printStackTrace()
                progressDialog.dismiss()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                        progressDialog.dismiss()
                    }
                })

        requestVolley.add(jsonObjectRequestPopular)

        var jsonObjectRequestBanner = JsonObjectRequest(Request.Method.GET, bannerURl, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try{

                val bannerData = response.getJSONArray("data")

                for (i in 0 until bannerData.length()){
                    bannerMaterial.add(bannerData.getJSONObject(i))
                }
                bannerAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }catch (e :JSONException){
                e.printStackTrace()
            }

        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("Debug", error.toString())
                    }

                })

        requestVolley.add(jsonObjectRequestBanner)

        var jsonObjectRequestCategory = JsonObjectRequest(Request.Method.GET, categoriesURL, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) =try{

                val categoryData = response.getJSONArray("data")

                for (i in 0 until categoryData.length()){
                    categoryAdapter.addJsonObject(categoryData.getJSONObject(i))
                    categoriesMaterial.add(categoryData.getJSONObject(i))
                }

                categoryAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("Debug", error.toString())
            }
        })
        requestVolley.add(jsonObjectRequestCategory)
    }
}// Required empty public constructor

class BannerAdapter(private val context: Context, private val banner: ArrayList<JSONObject>) : PagerAdapter() {

    var inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): View? {

        val view = inflater.inflate(R.layout.banner_slider, container, false)
        val bannerImage = view.findViewById<ImageView>(R.id.tourbannerImage)
        val bannerDesc = view?.findViewById<TextView>(R.id.tourbannerDesc)

        Picasso.with(context).load(banner.get(position).getString("banner_image")).into(bannerImage)
        bannerDesc?.text = banner.get(position).getString("banner_title")
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return banner.size
    }
}
