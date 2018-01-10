package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_tour_count.*
import java.io.LineNumberReader

class TourCountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_count)

        tourConfirm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@TourCountActivity, TourConfirmActivity::class.java))
            }
        })

        val userTime = ArrayList<SetTime>()
        prepareTime(userTime)
        val tourTimeAdapter = SelectTimeAdapter(this, userTime)
        val tourTimeLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourTime!!.layoutManager = tourTimeLayoutManager
        recycleTourTime!!.itemAnimator = DefaultItemAnimator()
        recycleTourTime!!.adapter = tourTimeAdapter

        val userCount = ArrayList<SetCount>()
        prepareCount(userCount)
        val tourCountAdapter = SelectCountAdapter(this, userCount)
        val tourCountLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleTourCount!!.layoutManager = tourCountLayoutManager
        recycleTourCount!!.itemAnimator = DefaultItemAnimator()
        recycleTourCount!!.adapter = tourCountAdapter
    }

    private fun prepareCount(userCount: ArrayList<SetCount>) {
        userCount.add(SetCount("Senior","RM 167"))
        userCount.add(SetCount("Adult","RM 147"))
        userCount.add(SetCount("Child","RM 127"))
    }

    private fun prepareTime(userTime: ArrayList<SetTime>) {
        userTime.add(SetTime("12:00"))
        userTime.add(SetTime("15:00"))
        userTime.add(SetTime("18:00"))
    }
}

class SetTime(val selectTourTime: String)

class SetCount(val selectTourCat: String, val selectTourCatPrice: String)
