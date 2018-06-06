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
import android.widget.*
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
import my.com.itrain.big_car.R.id.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormatSymbols
import java.util.*

class TourCountActivity : AppCompatActivity() {

    var tourURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"
    private var tour_name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_count)

        setSupportActionBar(toolbarCount)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val service_id = intent.getIntExtra("service_id",0)
        val package_id = intent.getStringExtra("package_id")
        val package_title = intent.getStringExtra("package_title")
        val package_pax = intent.getStringExtra("package_pax")
        val package_price = intent.getStringExtra("package_price")
        val onDay = intent.getStringExtra("dayOfWeek")
        val onDateYear = intent.getIntExtra("selectedYear",0)
        val onDateMonth = intent.getIntExtra("selectedMonth", 0)
        val onDateDay = intent.getIntExtra("selectedDay", 0)
        packageConfirmName?.text = package_title
        packageConfirmDate?.text = onDateDay.toString() + " " + DateFormatSymbols().getMonths()[onDateMonth - 1] + " " + onDateYear.toString()

        editPackage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val tourTimeAdapter = SelectTimeAdapter(this, onDay)
        val tourTimeLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourTime!!.layoutManager = tourTimeLayoutManager
        recycleTourTime!!.itemAnimator = DefaultItemAnimator()
        recycleTourTime!!.adapter = tourTimeAdapter

//        val tourCountAdapter = SelectCountAdapter(this)
//        val tourCountLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
//        recycleTourCount!!.layoutManager = tourCountLayoutManager
//        recycleTourCount!!.itemAnimator = DefaultItemAnimator()
//        recycleTourCount!!.adapter = tourCountAdapter

        val checkPay = findViewById<View>(R.id.add_to_cart_btn)
        checkPay.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (tourTimeAdapter.getSelectedItem().length() == 0) {
                    Toast.makeText(applicationContext, "Please Select The Tour Time", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this@TourCountActivity, TourConfirmActivity::class.java)
                    try {
                        intent.putExtra("service_id", service_id)
                        intent.putExtra("tour_name", tour_name)
                        intent.putExtra("package_id", package_id)
                        intent.putExtra("package_title", package_title)
                        intent.putExtra("package_pax" , package_pax)
                        intent.putExtra("package_price", package_price)
                        intent.putExtra("travel_date", onDateYear.toString() + "-" + onDateMonth.toString() + "-" + onDateDay.toString())
                        intent.putExtra("selectedYear", onDateYear)
                        intent.putExtra("selectedMonth", onDateMonth)
                        intent.putExtra("selectedDay", onDateDay)
                        intent.putExtra("travel_time", tourTimeAdapter.getSelectedItem().getString("time"))
                        intent.putExtra("travel_day_id", tourTimeAdapter.getSelectedItem().getString("day_id"))
                        intent.putExtra("travel_time_id", tourTimeAdapter.getSelectedItem().getString("time_id"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    startActivity(intent)
                }
            }
        })

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()


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

                    //tourCountAdapter.addJsonObject(packageMaterial)
                    //tourCountAdapter.notifyDataSetChanged()

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
