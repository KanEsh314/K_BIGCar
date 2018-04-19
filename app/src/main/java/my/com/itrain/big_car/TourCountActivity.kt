package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tour_count.*
import kotlinx.android.synthetic.main.activity_tour_detail.*
import kotlinx.android.synthetic.main.tourselect_time.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class TourCountActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"
    private var package_name: String = ""
    private var package_pax: String = ""
    private var tour_name: String = ""
    private var package_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_count)

        setSupportActionBar(toolbarCount)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val service_id = intent.getIntExtra("service_id",0)
        val selected_package = JSONArray(intent.getStringExtra("selected_package"))
        val onDateYear = intent.getIntExtra("selectedYear",0)
        val onDateMonth = intent.getIntExtra("selectedMonth", 0)
        val onDateDay = intent.getIntExtra("selectedDay", 0)

        try {
            for (i in 0 until selected_package.length()){
                packageConfirmName.text = selected_package.getJSONObject(i).getString("package_name")
                package_id = selected_package.getJSONObject(i).getString("package_id")
                package_name = selected_package.getJSONObject(i).getString("package_name")
                package_pax = selected_package.getJSONObject(i).getString("package_pax")
                Log.d("Debug", selected_package.getJSONObject(i).toString())
            }
            packageConfirmDate.text = onDateYear.toString()+"-"+onDateMonth.toString()+"-"+onDateDay.toString()
        }catch (e : Exception){
            e.printStackTrace()
        }

        editPackage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }

        })


//        tourTime.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener() {
//            fun onItemSelected(parent:AdapterView<*>, view:View, pos:Int, id:Long) {
//                val item = (view.findViewById(R.id.offer_type_txt) as TextView).getText().toString()
//                selectedOffer.setText(item)
//            }
//            fun onNothingSelected(parent:AdapterView<*>) {
//            }
//        })

        val checkPay = findViewById<View>(R.id.add_to_cart_btn)
        checkPay.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@TourCountActivity, TourConfirmActivity::class.java)
                try{
                    intent.putExtra("service_id", service_id)
                    intent.putExtra("tour_name", tour_name)
                    intent.putExtra("package_id", package_id)
                    intent.putExtra("package_name", package_name)
                    intent.putExtra("package_pax", package_pax)
                    intent.putExtra("travel_date", onDateYear.toString()+"-"+onDateMonth.toString()+"-"+onDateDay.toString())
                    //travel_day
                    //travel_time
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val tourTimeAdapter = SelectTimeAdapter(this)
        val tourTimeLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourTime!!.layoutManager = tourTimeLayoutManager
        recycleTourTime!!.itemAnimator = DefaultItemAnimator()
        recycleTourTime!!.adapter = tourTimeAdapter

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,tourURL+service_id,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val tourData = response.getJSONObject("data")
                    tour_name = tourData.getString("product_name")

                    val tourTimeData = tourData.getJSONArray("times")
                    for (i in 0 until tourTimeData.length()){
                        tourTimeAdapter.addJsonObject(tourTimeData.getJSONObject(i))
                    }

                    tourTimeAdapter.notifyDataSetChanged()
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

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
