package my.com.itrain.big_car


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_explore_content.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    var toursURL = "https://gentle-atoll-11837.herokuapp.com/api/tours"
    var categoriesURL = "http://gentle-atoll-11837.herokuapp.com/api/categories"
    var bannerURl = "https://gentle-atoll-11837.herokuapp.com/api/banners"
    private val toursMaterial = ArrayList<JSONObject>()
    private val categoriesMaterial = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore_content, container, false)
        setHasOptionsMenu(true);

        var newHeight = (Resources.getSystem().displayMetrics.heightPixels)/2
        var bannerImage = view.findViewById<ImageView>(R.id.tourbannerImage)
        bannerImage.requestLayout()
        bannerImage.layoutParams.height = newHeight

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.explore_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<Trend>()
        prepareList(list)
        val popularAdapter = ExploreContentAdapter(this,list)
        val popularLayoutManager = GridLayoutManager(this.activity,2)
        recycleViewActivities!!.layoutManager = popularLayoutManager
        recycleViewActivities!!.itemAnimator = DefaultItemAnimator()
        recycleViewActivities!!.adapter = popularAdapter

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this.context)

        val destinationAdapter = ExplorePlaceContentAdapter(context ,object: ExplorePlaceContentAdapter.OnItemClickListener{
            override fun onItemClick(position:Int){
                val intent = Intent(context,TourDetailActivity::class.java)
                try {
                    intent.putExtra("serviceid", toursMaterial.get(position).getInt("service_id"))
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

        val categoryAdapter = CategoryAdapter(context, object : CategoryAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(context, CategoryActivity::class.java)
                try {
                    intent.putExtra("servicecatid", categoriesMaterial.get(position).getInt("servicecat_id"))
                    Log.d("Debug", categoriesMaterial.get(position).getInt("servicecat_id").toString())
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

        val progressDialog = ProgressDialog(context, R.style.DialogTheme)
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

        var jsonObjectRequestBanner = JsonObjectRequest(Request.Method.GET, bannerURl, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try{

                val bannerData = response.getJSONArray("data")

                for (i in 0 until bannerData.length()){
                    Picasso.with(context).load(bannerData.getJSONObject(i).getString("banner_image")).into(tourbannerImage)
                    tourbannerDesc.text = bannerData.getJSONObject(i).getString("banner_title")
                }

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

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.tour2,"Crime History","MYR198"))
        list.add(Trend(R.drawable.tour1,"Crime History","MYR98"))
        list.add(Trend(R.drawable.tour2,"Crime History","MYR198"))
        list.add(Trend(R.drawable.tour2,"Crime History","MYR98"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val price : String)