package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.favorite_content.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class FavoriteActivity : AppCompatActivity() {

    var favhisURL = "https://gentle-atoll-11837.herokuapp.com/api/favorite"
    var favoriteURL = "http://gentle-atoll-11837.herokuapp.com/api/favoritetour"
    private val favhisMaterial = ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setSupportActionBar(toolbarfavorite)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        val favhisAdapter = FavHisContent(this, object: FavHisContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, TourDetailActivity::class.java)
                try {
                    intent.putExtra("service_id", favhisMaterial.get(position).getInt("service_id"))
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        }, object : FavHisContent.sOnItemClickListener{
            override fun onItemClick(position: Int) {
                favLike.setOnClickListener(object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
                        if(applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).contains("myToken")){

                            val jsonBody = JSONObject()
                            try
                            {
                                jsonBody.put("service_id", favhisMaterial.get(position).getInt("service_id"))
                            }
                            catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            val stringRequest = object : JsonObjectRequest(Request.Method.POST, favoriteURL, jsonBody,object : Response.Listener<JSONObject>{
                                override fun onResponse(response: JSONObject) {
                                    if (response.getString("status") == "true"){
                                        favLike.setBackgroundResource(R.mipmap.ic_action_like)
                                    } else if (response.getString("status") == "false"){
                                        favLike.setBackgroundResource(R.mipmap.ic_action_unlike)
                                    }
                                    finish();
                                    startActivity(getIntent());
                                }
                            }, object : Response.ErrorListener{
                                override fun onErrorResponse(error: VolleyError?) {
                                    Log.d("Debug", error.toString())
                                }
                            }){
                                @Throws(AuthFailureError::class)
                                override fun getHeaders():Map<String,String>{
                                    val headers = HashMap<String, String>()
                                    headers.put("Authorization", "Bearer "+sharedPreferences)
                                    return headers
                                }
                            }

                            val requestVolley = Volley.newRequestQueue(applicationContext)
                            requestVolley.add(stringRequest)

                        }else{
                            startActivity(Intent(applicationContext, StartActivity::class.java))
                        }
                    }
                })
            }
        })


        val favhisLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleViewFavorite!!.layoutManager = favhisLayoutManager
        recycleViewFavorite!!.itemAnimator = DefaultItemAnimator()
        recycleViewFavorite!!.adapter = favhisAdapter

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, favhisURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val favhisData = response.getJSONArray("data");

                    for (i in 0 until favhisData.length()){
                        favhisAdapter.addJsonObject(favhisData.getJSONObject(i))
                        favhisMaterial.add(favhisData.getJSONObject(i))
                        Log.d("Debug", favhisData.getJSONObject(i).toString())
                    }
                    favhisAdapter.notifyDataSetChanged()
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
