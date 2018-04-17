package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import kotlinx.android.synthetic.main.activity_tour_count.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class TourCountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_count)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val packageSet = JSONArray(intent.getStringExtra("selectedPackage"))
        val tourService_id = intent.getIntExtra("tourService_id",0)
        val onDateYear = intent.getIntExtra("selectedYear",0)
        val onDateMonth = intent.getIntExtra("selectedMonth", 0)
        val onDateDay = intent.getIntExtra("selectedDay", 0)

        try {
            for (i in 0 until packageSet.length()){
                packageConfirmName.text = packageSet.getJSONObject(i).getString("package_name")
                Log.d("Debug", packageSet.getJSONObject(i).toString())
            }
            packageConfirmDate.text = onDateDay.toString()+"/"+onDateMonth.toString()+"/"+onDateYear.toString()
        }catch (e : Exception){
            e.printStackTrace()
        }

        editPackage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }

        })

        val checkPay = findViewById<View>(R.id.add_to_cart_btn)
        checkPay.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@TourCountActivity, TourConfirmActivity::class.java)
                try{
                    intent.putExtra("service_id", tourService_id)
                    intent.putExtra("travel_date", onDateDay.toString()+"/"+onDateMonth.toString()+"/"+onDateYear.toString())
                    //travel_day
                    //travel_time
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                startActivity(intent)
            }
        })

        val userTime = ArrayList<SetTime>()
        prepareTime(userTime)
        val tourTimeAdapter = SelectTimeAdapter(this, userTime)
        val tourTimeLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourTime!!.layoutManager = tourTimeLayoutManager
        recycleTourTime!!.itemAnimator = DefaultItemAnimator()
        recycleTourTime!!.adapter = tourTimeAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun prepareTime(userTime: ArrayList<SetTime>) {
        userTime.add(SetTime("12:00"))
        userTime.add(SetTime("15:00"))
        userTime.add(SetTime("18:00"))
        userTime.add(SetTime("12:00"))
        userTime.add(SetTime("15:00"))
        userTime.add(SetTime("18:00"))
    }
}

class SetTime(val selectTourTime: String)
