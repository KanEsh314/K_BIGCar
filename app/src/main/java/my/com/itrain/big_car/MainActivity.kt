package my.com.itrain.big_car

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
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
        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            override fun onTabSelected(tab : TabLayout.Tab){
                super.onTabSelected(tab)
                val tabIconColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
                tab!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                super.onTabUnselected(tab)
                val tabIconColor = ContextCompat.getColor(this@MainActivity, R.color.colorDark)
                tab!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                super.onTabReselected(tab)
            }
        })
        setupTabIcons()

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.drawerArrowDrawable.setColor(resources.getColor(R.color.colorAccent))
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
}

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                onBackPressed()
            }
            R.id.nav_login -> {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
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
        tabs.getTabAt(0)!!.setIcon(R.mipmap.ic_ptp)
        tabs.getTabAt(1)!!.setIcon(R.mipmap.ic_tour)
        tabs.getTabAt(2)!!.setIcon(R.mipmap.ic_near)
        tabs.getTabAt(3)!!.setIcon(R.mipmap.ic_me)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
