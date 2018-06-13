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
import kotlinx.android.synthetic.main.fragment_acompleted.*
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class ACompletedFragment : Fragment() {

    var acompletedbookingURL = "https://gentle-atoll-11837.herokuapp.com/api/tripcompletehistory"
    private val acompletedMaterial = ArrayList<JSONObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acompleted, container, false)
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
                val intent = Intent(activity, BookingDetailActivity::class.java)
                try {
                    intent.putExtra("booking_id", acompletedMaterial.get(position).getInt("booking_id"))
                } catch (e: JSONException){
                    e.printStackTrace()
                }
                startActivity(intent);
            }
        })
        val completedLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, true)
        recycleViewACompleted!!.layoutManager = completedLayoutManager
        recycleViewACompleted!!.itemAnimator = DefaultItemAnimator()
        recycleViewACompleted!!.adapter = completedAdapter

        val sharedPreferences = activity.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, acompletedbookingURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val acompletedData = response.getJSONArray("data");

                    for (i in 0 until acompletedData.length()){
                        completedAdapter.addJsonObject(acompletedData.getJSONObject(i))
                        acompletedMaterial.add(acompletedData.getJSONObject(i))
                        Log.d("Debug", acompletedData.getJSONObject(i).toString())
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
