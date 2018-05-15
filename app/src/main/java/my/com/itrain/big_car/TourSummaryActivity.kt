package my.com.itrain.big_car

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tour_summary.*
import my.com.itrain.big_car.R.id.*

class TourSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_summary)

        tour_name?.text = intent.getStringExtra("tour_name")
        package_name?.text = intent.getStringExtra("package_title")
        travel_date?.text = intent.getStringExtra("travel_date")
        travel_time?.text = intent.getStringExtra("travel_time")
        traveller_count?.text = intent.getStringExtra("package_pax")
        booking_name?.text = intent.getStringExtra("booking_name")
        booking_mobile_number?.text = intent.getStringExtra("mobile_number")
        booking_nationality?.text = intent.getStringExtra("nationality")
        booking_email?.text = intent.getStringExtra("user_email")
        //Toast.makeText(applicationContext, booked_passenger.toString(), Toast.LENGTH_LONG).show()

        more_trips.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })

//        val summaryAdapter = SummaryAdapter(applicationContext)
//        val packegeOptionLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
//        passengerDetailsRecyclerView!!.layoutManager = packegeOptionLayoutManager
//        passengerDetailsRecyclerView!!.itemAnimator = DefaultItemAnimator()
//        passengerDetailsRecyclerView!!.adapter = summaryAdapter
    }
}
