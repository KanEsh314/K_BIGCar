package my.com.itrain.big_car


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_anew.*
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class ANewFragment : Fragment() {

    var anewbookingURL = "https://gentle-atoll-11837.herokuapp.com/api/usernewhistory"
    private val anewMaterial = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anew, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(activity)

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val bookingAdapter = BookingContent(activity, object: BookingContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, BookingDetailActivity::class.java)
                try {
                    intent.putExtra("booking_id", anewMaterial.get(position).getInt("booking_id"))
                } catch (e: JSONException){
                    e.printStackTrace()
                }
                startActivity(intent);
            }
        })
        val bookingLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true)
        recycleViewANew!!.layoutManager = bookingLayoutManager
        recycleViewANew!!.itemAnimator = DefaultItemAnimator()
        recycleViewANew!!.adapter = bookingAdapter

        val sharedPreferences = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, anewbookingURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val favhisData = response.getJSONArray("data");

                    for (i in 0 until favhisData.length()){
                        bookingAdapter.addJsonObject(favhisData.getJSONObject(i))
                        anewMaterial.add(favhisData.getJSONObject(i))
                        Log.d("Debug", favhisData.getJSONObject(i).toString())
                    }
                    bookingAdapter.notifyDataSetChanged()
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
                        progressDialog.dismiss()
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
}
