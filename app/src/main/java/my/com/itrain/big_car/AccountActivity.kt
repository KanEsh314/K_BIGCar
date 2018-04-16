package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_tour_count.*
import kotlinx.android.synthetic.main.fragment_profile_content.*
import my.com.itrain.big_car.R.drawable.user
import org.json.JSONException
import org.json.JSONObject

class AccountActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var userURL = "http://gentle-atoll-11837.herokuapp.com/user"
    var updateURL = "http://gentle-atoll-11837.herokuapp.com/api/updateuser/"

    var nameHolder:String = ""
    var numberHolder:String = ""
    var addressHolder:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setSupportActionBar(account_toolbar)

        val progressDialog = ProgressDialog(applicationContext, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait")

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                name.text = Editable.Factory.getInstance().newEditable(userInfo.getString("name"))
                hp_nbr.text = Editable.Factory.getInstance().newEditable(userInfo.getString("phonenumber"))
                address.text = Editable.Factory.getInstance().newEditable(userInfo.getString("address"))
                progressDialog.dismiss()
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("Debug", error.toString())
                progressDialog.dismiss()
            }

        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
        }

        val requestVolley = Volley.newRequestQueue(applicationContext)
        requestVolley.add(jsonRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    userUpadate()
                    try{

                    }catch (e : JSONException){

                    }
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun userUpadate() {
        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server")
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, updateURL+"111", object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                val sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
                try{
                    val myToken = JSONObject(response)
                    sharedPreferences.edit().putString("myToken",myToken.getJSONObject("result").getString("token")).commit()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }catch (e : JSONException){
                    e.printStackTrace()
                }
                progressDialog.dismiss()
                //Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("name", nameHolder)
                params.put("ic","")
                params.put("phonenumber", numberHolder)
                params.put("email", "")
                params.put("address", addressHolder)
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(this)
        requestVolley.add(stringRequest)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun CheckEditTextIsEmptyOrNot() {

        nameHolder = name.text.toString()
        numberHolder = hp_nbr.text.toString()
        addressHolder = address.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(numberHolder) || TextUtils.isEmpty(addressHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}
