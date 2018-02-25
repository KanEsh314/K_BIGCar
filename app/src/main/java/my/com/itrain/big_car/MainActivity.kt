package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var pagerAdapter: CustomPagerAdapter?=null
    private val tabIcons = intArrayOf(R.drawable.ic_explore,
                                      R.drawable.ic_saved,
                                      R.drawable.ic_trip,
                                      R.drawable.ic_inbox,
                                      R.drawable.ic_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(ExploreContentFragment(), "Explore")
        pagerAdapter!!.addFragments(BrowseContentFragment(), "Browse")
        pagerAdapter!!.addFragments(TripsContentFragment(), "Trips")
        pagerAdapter!!.addFragments(InboxContentFragment(), "Inbox")
        pagerAdapter!!.addFragments(ProfileContentFragment(),"Profile")

        viewpager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewpager)
        setupTabIcons()
    }

    private fun setupTabIcons() {
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_saved)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_trip)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_inbox)
        tabs.getTabAt(4)!!.setIcon(R.drawable.ic_profile)
    }
}
