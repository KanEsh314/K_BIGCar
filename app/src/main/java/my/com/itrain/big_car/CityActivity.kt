package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_city.*
import org.json.JSONException
import org.json.JSONObject

class CityActivity : AppCompatActivity() {

    var cityURL = "https://gentle-atoll-11837.herokuapp.com/api/tourlocation/"
    private val cityMaterial = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        setSupportActionBar(toolbarCity)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val stateservice_id = intent.getIntExtra("servicestateid",0)
        titleCity?.text = intent.getStringExtra("stateTitle")

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val trendingAdapter = ExplorePopularContent(this, object: ExplorePopularContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, TourDetailActivity::class.java)
                try{
                    intent.putExtra("service_id", cityMaterial.get(position).getInt("service_id"))
                }catch (e :JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })
        val trendingLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleViewCity!!.layoutManager = trendingLayoutManager
        recycleViewCity!!.itemAnimator = DefaultItemAnimator()
        recycleViewCity!!.adapter = trendingAdapter

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, cityURL+stateservice_id, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val categoryData = response.getJSONArray("data");

                    for (i in 0 until categoryData.length()){
                        trendingAdapter.addJsonObject(categoryData.getJSONObject(i))
                        cityMaterial.add(categoryData.getJSONObject(i))
                    }
                    trendingAdapter.notifyDataSetChanged()
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
