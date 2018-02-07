package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mMap:GoogleMap
    var pagerAdapter: CustomPagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar);

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(ExploreContentFragment())
        pagerAdapter!!.addFragments(BrowseContentFragment())
        pagerAdapter!!.addFragments(TripsContentFragment())
        pagerAdapter!!.addFragments(InboxContentFragment())
        pagerAdapter!!.addFragments(ProfileContentFragment())

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val item = menu!!.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        return true
    }
}
