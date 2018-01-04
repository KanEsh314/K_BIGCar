package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TourDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        val checkDates = findViewById<View>(R.id.add_to_cart_btn)
        checkDates.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@TourDetailActivity, TourDatesActivity::class.java))
            }
        })
    }
}
