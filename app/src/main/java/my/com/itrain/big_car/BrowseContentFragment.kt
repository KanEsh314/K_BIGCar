package my.com.itrain.big_car


import android.app.VoiceInteractor
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_browse_content.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment() {

    var categoryURL = "http://gentle-atoll-11837.herokuapp.com/api/categories"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_browse_content, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(context)
        listViewCategory!!.adapter = categoryAdapter

        listViewCategory.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                startActivity(Intent(context, CategoryActivity::class.java))
            }
        })

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this.activity)

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, categoryURL, null, object : Response.Listener<JSONObject> {
            override fun onResponse(response: JSONObject) =try{

                val categoryData = response.getJSONArray("data")

                for (i in 0 until categoryData.length()){
                    categoryAdapter.addJsonObject(categoryData.getJSONObject(i))
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
        requestVolley.add(jsonObjectRequest)
    }

}// Required empty public constructor
