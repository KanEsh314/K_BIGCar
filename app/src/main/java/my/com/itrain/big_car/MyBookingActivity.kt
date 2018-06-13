package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_my_booking.*
import java.util.ArrayList

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking)

        setSupportActionBar(toolbarbooking)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val pagerAdapter = CustomAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(CompletedFragment(),"Completed")
        pagerAdapter!!.addFragments(NewFragment(), "New")

        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class CustomAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    var mFm = fm
    var mFragmentItems: ArrayList<Fragment> = ArrayList()
    var mFragmentTitle: ArrayList<String> = ArrayList()

    fun addFragments(fragment: Fragment, fragmentTitle: String){
        mFragmentItems.add(fragment)
        mFragmentTitle.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.count()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitle[position]
    }

}
