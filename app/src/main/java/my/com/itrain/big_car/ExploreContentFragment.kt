package my.com.itrain.big_car


import android.app.VoiceInteractor
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_explore_content.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tours"

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

        val destinationAdapter = ExplorePlaceContentAdapter(activity ,object: ExplorePlaceContentAdapter.OnItemClickListener{
            override fun onItemClick(position:Int){
                startActivity(Intent(context,TourDetailActivity::class.java))
            }
        })
        val destinationLayoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,true)
        recycleViewDestination!!.layoutManager = destinationLayoutManager
        recycleViewDestination!!.itemAnimator = DefaultItemAnimator()
        recycleViewDestination!!.adapter = destinationAdapter

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this.context)

        var jsonArrayRequest = JsonObjectRequest(Request.Method.GET, tourURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val tourData= response.getJSONArray("data")

                for (i in 0 until tourData.length()){
                    destinationAdapter.addJsonObject(tourData.getJSONObject(i))
                }

                destinationAdapter.notifyDataSetChanged()
            } catch (e : JSONException){
                e.printStackTrace()
            }

        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonArrayRequest)
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val price : String)
