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
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account.*
import org.json.JSONObject

class AccountActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var userURL = "https://gentle-atoll-11837.herokuapp.com/api/user"
    var updateURL = "https://gentle-atoll-11837.herokuapp.com/api/updateuser"

    var firstnameHolder:String = ""
    var lastnameHolder:String = ""
    var numberHolder:String = ""
    var addressHolder:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setSupportActionBar(account_toolbar)

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        var jsonRequest = object  : JsonObjectRequest(Request.Method.GET, userURL, null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                val userInfo = response.getJSONObject("data")
                if (userInfo.getString("profilepic") == ""){
                    profilePicture.setImageResource(R.mipmap.ic_app_user)
                }else{
                    Picasso.with(applicationContext).load(userInfo.getString("profilepic")).into(profilePicture)
                }
                first_name.text = Editable.Factory.getInstance().newEditable(userInfo.getString("first_name"))
                last_name.text = Editable.Factory.getInstance().newEditable(userInfo.getString("last_name"))
                hp_nbr.text = Editable.Factory.getInstance().newEditable(userInfo.getString("phonenumber"))
                address.text = Editable.Factory.getInstance().newEditable(userInfo.getString("address"))
            }

        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError) {
                Log.d("Debug", error.toString())
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
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun userUpadate() {

        val sharedPreferences = applicationContext.getSharedPreferences("myPref", Context.MODE_PRIVATE).getString("myToken","")
        val stringRequest = object : StringRequest(Request.Method.POST, updateURL, object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                Log.d("Debug", response)
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("Debug", error.toString())
            }
        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String,String>{
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer "+sharedPreferences)
                return headers
            }
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("first_name", firstnameHolder)
                params.put("last_name", lastnameHolder)
                params.put("phonenumber", numberHolder)
                params.put("address", addressHolder)
                Log.d("Update", params.toString())
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

        firstnameHolder = first_name.text.toString()
        lastnameHolder = last_name.text.toString()
        numberHolder = hp_nbr.text.toString()
        addressHolder = address.text.toString()

        if (TextUtils.isEmpty(firstnameHolder) || TextUtils.isEmpty(lastnameHolder) || TextUtils.isEmpty(numberHolder) || TextUtils.isEmpty(addressHolder)){
            CheckEditText = false
        }else {
            CheckEditText = true
        }
    }
}
