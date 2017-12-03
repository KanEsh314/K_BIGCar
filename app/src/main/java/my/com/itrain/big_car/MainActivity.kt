package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var pagerAdapter: CustomPagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar);

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(ExploreContentFragment(), "Explore")
        pagerAdapter!!.addFragments(SavedContentFragment(), "Saved")

        viewpager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewpager)
    }
}
