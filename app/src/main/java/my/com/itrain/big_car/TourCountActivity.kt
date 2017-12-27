package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TourCountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_count)

        val tourNext = findViewById<View>(R.id.nextConfirm)
        tourNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@TourCountActivity, TourConfirmActivity::class.java))
            }
        })
    }
}
