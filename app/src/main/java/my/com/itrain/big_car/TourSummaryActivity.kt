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
import org.json.JSONException
import org.json.JSONObject

class TourSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_summary)

        tour_name?.text = intent.getStringExtra("tour_name")
        package_name?.text = intent.getStringExtra("package_title")
        travel_date?.text = intent.getStringExtra("travel_date")
        travel_time?.text = intent.getStringExtra("travel_time")
        traveller_count?.text = intent.getStringExtra("package_pax")
        booking_name?.text = intent.getStringExtra("first_name")+" "+intent.getStringExtra("last_name")
        booking_mobile_number?.text = intent.getStringExtra("mobile_number")
        booking_nationality?.text = intent.getStringExtra("nationality")
        booking_email?.text = intent.getStringExtra("user_email")

        if (intent.getStringExtra("remark") == "") {
            booking_remark?.text = "No Remark"
        } else {
            booking_remark?.text = intent.getStringExtra("remark")
        }

        if (intent.getStringExtra("paymentID") == "") {
            booking_id.text = "Payment ID is not available"
        } else {
            booking_id?.text = intent.getStringExtra("paymentID")
        }

        if (intent.getStringExtra("paymentState") == "") {
            booking_state?.text = "Pending"
        } else {
            booking_state?.text = intent.getStringExtra("paymentState")
        }

        booking_amount?.text = "RM"+intent.getStringExtra("package_price")


        more_trips.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        Toast.makeText(applicationContext, "Please Click More Tour", Toast.LENGTH_LONG).show()
    }
}
