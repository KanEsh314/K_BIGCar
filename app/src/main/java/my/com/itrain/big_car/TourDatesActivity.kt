package my.com.itrain.big_car

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_tour_dates.*
import kotlinx.android.synthetic.main.fragment_explore_content.*
import java.util.*

class TourDatesActivity : AppCompatActivity() {

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_dates)

        //OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

//        myFAB.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                DatePickerDialog(this@TourDatesActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
//            }
//        })

        val packageOptions = ArrayList<PackageTour>()
        preparePackage(packageOptions)
        val packageOptionAdapter = PackageAdapter(this, packageOptions, object: PackageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                startActivity(Intent(this@TourDatesActivity, TourCountActivity::class.java))
            }
        })
        val packegeOptionLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        packageRecyclerView!!.layoutManager = packegeOptionLayoutManager
        packageRecyclerView!!.itemAnimator = DefaultItemAnimator()
        packageRecyclerView!!.adapter = packageOptionAdapter

    }

    private fun preparePackage(packageOptions: ArrayList<PackageTour>) {
        packageOptions.add(PackageTour("Peak Tram + Sky Pass","RM 36"))
        packageOptions.add(PackageTour("Chocolate Museum","RM 51"))
    }

}

class PackageTour(val packageName : String, val packagePrice : String)