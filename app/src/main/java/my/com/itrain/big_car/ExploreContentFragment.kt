package my.com.itrain.big_car


import android.app.ProgressDialog
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
    private val toursMaterial = ArrayList<JSONObject>()
    var bannerURl = "https://gentle-atoll-11837.herokuapp.com/api/banners"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore_content, container, false)

        var newHeight = (Resources.getSystem().displayMetrics.heightPixels)/2
        var bannerImage = view.findViewById<ImageView>(R.id.tourbannerImage)
        bannerImage.requestLayout()
        bannerImage.layoutParams.height = newHeight

        return view
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
                    //Log.d("Debug",toursMaterial.get(position).getInt("service_id").toString())
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })
        val destinationLayoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL,true)
        recycleViewDestination!!.layoutManager = destinationLayoutManager
        recycleViewDestination!!.itemAnimator = DefaultItemAnimator()
        recycleViewDestination!!.adapter = destinationAdapter

        //VOLLEY

        val progressDialog = ProgressDialog(context, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, toursURL,null, object : Response.Listener<JSONObject>{
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
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequest)

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
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val price : String)