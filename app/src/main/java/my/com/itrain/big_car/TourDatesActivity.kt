package my.com.itrain.big_car

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_tour_dates.*
import kotlinx.android.synthetic.main.fragment_explore_content.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class TourDatesActivity : AppCompatActivity() {

    var packageURL = "https://gentle-atoll-11837.herokuapp.com/api/tour/"
    val packageMaterial = ArrayList<JSONObject>()
    val calender = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_dates)

        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val service_id = intent.getIntExtra("service_id", 0)
        val mYear = calender.get(Calendar.YEAR)
        val mMonth = calender.get(Calendar.MONTH)
        val mDate = calender.get(Calendar.DAY_OF_MONTH)

        //Volley
        val requestVolley = Volley.newRequestQueue(this)

        val packageOptionAdapter = PackageAdapter(this, object: PackageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //onDate
                val dateListener = DatePickerDialog(this@TourDatesActivity,R.style.DialogTheme, object : DatePickerDialog.OnDateSetListener{
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                        val simpledateformat = SimpleDateFormat("EEEE")
                        val date = Date(year, month, dayOfMonth-1)
                        val dayOfWeek = simpledateformat.format(date)
                        Log.d("Debug", dayOfWeek.toString())

                        val intent = Intent(this@TourDatesActivity, TourCountActivity::class.java)
                        try {
                            intent.putExtra("service_id", service_id)
                            intent.putExtra("package_id", packageMaterial.get(position).get("package_id").toString())
                            intent.putExtra("package_name", packageMaterial.get(position).get("package_name").toString())
                            intent.putExtra("package_pax", packageMaterial.get(position).get("package_pax").toString())
                            intent.putExtra("selectedYear", year)
                            intent.putExtra("selectedMonth", month+1)
                            intent.putExtra("selectedDay", dayOfMonth)
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                        startActivity(intent)
                    }
                }, mYear, mMonth, mDate)
                dateListener.show()
                dateListener.datePicker.minDate = calender.timeInMillis
            }
        })
        val packegeOptionLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        packageRecyclerView!!.layoutManager = packegeOptionLayoutManager
        packageRecyclerView!!.itemAnimator = DefaultItemAnimator()
        packageRecyclerView!!.adapter = packageOptionAdapter

        //Volley
        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, packageURL+service_id, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val packageData = response.getJSONObject("data")
                val tourPackageData = packageData.getJSONArray("tour_packages")

                for (i in 0 until tourPackageData.length()){
                    packageOptionAdapter.addJsonObject(tourPackageData.getJSONObject(i))
                    packageMaterial.add(tourPackageData.getJSONObject(i))
                }

                packageOptionAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }catch (e : JSONException){
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
