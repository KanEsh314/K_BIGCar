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
import kotlinx.android.synthetic.main.activity_near_by.*
import org.json.JSONException
import org.json.JSONObject

class NearByActivity : AppCompatActivity() {

    var nearByTourURL = "https://gentle-atoll-11837.herokuapp.com/api/nearby/"
    var nearByMaterial = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_by)

        setSupportActionBar(toolBarNearBy)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val attraction_id = intent.getIntExtra("attraction_id",0)

        val NearAdapter = NearByAdapter(this, object : NearByAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, TourDetailActivity::class.java)
                try{
                    intent.putExtra("service_id", nearByMaterial.get(position).getInt("service_id"))
                }catch (e: JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })
        val nearbyLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        nearByRecyclerView!!.layoutManager = nearbyLayoutManager
        nearByRecyclerView!!.itemAnimator = DefaultItemAnimator()
        nearByRecyclerView!!.adapter = NearAdapter

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()


        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,nearByTourURL+attraction_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val nearByTourData = response.getJSONArray("data")

                    for (i in 0 until nearByTourData.length()){
                        NearAdapter.addJsonObject(nearByTourData.getJSONObject(i))
                        nearByMaterial.add(nearByTourData.getJSONObject(i))
                    }
                    NearAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                }catch (e : JSONException){
                    e.printStackTrace()
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

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
