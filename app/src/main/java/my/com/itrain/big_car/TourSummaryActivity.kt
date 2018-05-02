package my.com.itrain.big_car

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_tour_summary.*

class TourSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_summary)

        tour_name?.text = intent.getStringExtra("tour_name")
        package_name?.text = intent.getStringExtra("package_name")
        travel_date?.text = intent.getStringExtra("travel_date")
        travel_time?.text = intent.getStringExtra("travel_time")
        traveller_count?.text = intent.getStringExtra("package_pax")
        booking_name?.text = intent.getStringExtra("booking_name")
        booking_mobile_number?.text = intent.getStringExtra("mobile_number")
        booking_nationality?.text = intent.getStringExtra("nationality")
        booking_email?.text = intent.getStringExtra("user_email")
        passenger_name?.text = intent.getStringExtra("passenger_name")
        passenger_ic_passport?.text = intent.getStringExtra("ic_passport")
    }
}
