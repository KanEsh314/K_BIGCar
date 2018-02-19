package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_tour_detail.*

class TourDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val checkDates = findViewById<View>(R.id.add_to_cart_btn)
        checkDates.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@TourDetailActivity, TourDatesActivity::class.java))
            }
        })

        val userreview = ArrayList<Review>()
        prepareReview(userreview)
        val reviewAdapter = ReviewContentAdapter(this, userreview)
        val reviewLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourReview!!.layoutManager = reviewLayoutManager
        recycleTourReview!!.itemAnimator = DefaultItemAnimator()
        recycleTourReview!!.adapter = reviewAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun prepareReview(userreview: ArrayList<Review>) {
        userreview.add(Review(R.drawable.tour2, "Kanesh", 1F, "1.0", "This Tour Look Good"))
        userreview.add(Review(R.drawable.tour1, "Kanesh", 2F, "2.0", "This Tour & Hotel Look Good"))
        userreview.add(Review(R.drawable.tour2, "Kanesh", 1F, "1.0", "This Tour Look Good"))
        userreview.add(Review(R.drawable.tour1, "Kanesh", 2F, "2.0", "This Tour & Hotel Look Good"))
    }
}

class Review(val userPic : Int, val reviewName : String, val ratingBarStar : Float, val ratingBarText : String, val userReview : String)
