package my.com.itrain.big_car


import android.app.ProgressDialog
import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_completed.*
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class CompletedFragment : Fragment() {

    var completedbookingURL = "http://gentle-atoll-11837.herokuapp.com/api/userhistory"
    private val completedMaterial = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(activity)

        val progressDialog = ProgressDialog(activity, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val completedAdapter = BookingContent(activity, object: BookingContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Log.d("Next Page","Will Have Soon")
            }
        })
        val completedLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true)
        recycleViewCompleted!!.layoutManager = completedLayoutManager
        recycleViewCompleted!!.itemAnimator = DefaultItemAnimator()
        recycleViewCompleted!!.adapter = completedAdapter

        val sharedPreferences = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, completedbookingURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val favhisData = response.getJSONArray("data");

                    for (i in 0 until favhisData.length()){
                        completedAdapter.addJsonObject(favhisData.getJSONObject(i))
                        completedMaterial.add(favhisData.getJSONObject(i))
                        Log.d("Debug", favhisData.getJSONObject(i).toString())
                    }
                    completedAdapter.notifyDataSetChanged()
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
