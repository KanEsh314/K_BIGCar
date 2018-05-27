package my.com.itrain.big_car

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.net.Uri
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
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.content_main.*
import my.com.itrain.big_car.R.id.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    var pagerAdapter: CustomPagerAdapter?=null
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(BrowseContentFragment(), "P2P")
        pagerAdapter!!.addFragments(ExploreContentFragment(), "TOUR")
        pagerAdapter!!.addFragments(TripsContentFragment(), "NEARBY")
        pagerAdapter!!.addFragments(ProfileContentFragment(),"ME")


        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            override fun onTabSelected(tab : TabLayout.Tab){
                super.onTabSelected(tab)
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                tab!!.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                super.onTabUnselected(tab)
                val tabIconColor = ContextCompat.getColor(applicationContext, R.color.colorDark)
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
                if (applicationContext.getSharedPreferences("myPref", MODE_PRIVATE).getString("myToken","") == ""){
                    startActivity(Intent(applicationContext, StartActivity::class.java))
                }
            }
            R.id.nav_review -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+packageName)))
                }catch (e: ActivityNotFoundException){
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+packageName)))
                }
            }
            R.id.nav_feedback -> {
                startActivity(Intent(applicationContext, FeedbackActivity::class.java))
            }
            R.id.nav_contact_us -> {
                startActivity(Intent(applicationContext, ContactActivity::class.java))
            }
            R.id.nav_setting -> {

            }
            R.id.nav_privacy -> {
                startActivity(Intent(applicationContext, ConditionActivity::class.java))
            }
            R.id.nav_term -> {
                startActivity(Intent(applicationContext, TermsActivity::class.java))
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
