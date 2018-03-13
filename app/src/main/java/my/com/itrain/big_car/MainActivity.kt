package my.com.itrain.big_car

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    var pagerAdapter: CustomPagerAdapter?=null
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(BrowseContentFragment(), "PTP")
        pagerAdapter!!.addFragments(ExploreContentFragment(), "TOUR")
        pagerAdapter!!.addFragments(TripsContentFragment(), "NEARBY")
        pagerAdapter!!.addFragments(ProfileContentFragment(),"ME")

        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
        setupTabIcons()

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
}

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_login -> {

            }
            R.id.nav_review -> {

            }
            R.id.nav_card -> {

            }
            R.id.nav_currency -> {

            }
            R.id.nav_language -> {

            }
            R.id.nav_notify -> {

            }
            R.id.nav_feedback -> {

            }
            R.id.nav_contact_us -> {

            }
            R.id.nav_setting -> {

            }
            R.id.nav_privacy -> {

            }
            R.id.nav_term -> {

            }
            R.id.nav_careers -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupTabIcons() {
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_saved)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_trip)
        tabs.getTabAt(3)!!.setIcon(R.drawable.ic_profile)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
