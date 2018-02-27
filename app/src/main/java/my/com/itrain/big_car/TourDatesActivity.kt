package my.com.itrain.big_car

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.LinearLayout
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

        val tourService_id = intent.getIntExtra("serviceid", 0)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker,year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, monthOfYear)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        //Volley
        val requestVolley = Volley.newRequestQueue(this)

        val packageOptionAdapter = PackageAdapter(this, object: PackageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                //OnDateSetListener
                val dateOnTour = DatePickerDialog(this@TourDatesActivity, R.style.DialogTheme, dateSetListener, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))
                dateOnTour.show()

                dateOnTour.setButton(DialogInterface.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(this@TourDatesActivity, TourCountActivity::class.java)
                        try {
                            intent.putExtra("selectedPackage", packageMaterial.get(position).getString("tour_packages"))
                            Log.d("Debug", calender.get(Calendar.DAY_OF_MONTH).toString())
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                        startActivity(intent)
                    }

                })
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

        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET, packageURL+tourService_id, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val packageData = response.getJSONObject("data")
                packageMaterial.add(packageData)
                val tourPackageData = packageData.getJSONArray("tour_packages")


                for (i in 0 until tourPackageData.length()){
                    packageOptionAdapter.addJsonObject(tourPackageData.getJSONObject(i))
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
