package my.com.itrain.big_car

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : AppCompatActivity() {

    var feedbackURL = "https://gentle-atoll-11837.herokuapp.com/api/feedback"
    var CheckEditText:Boolean = false

    var nameHolder:String = ""
    var emailHolder:String = ""
    var phoneHolder: String = ""
    var messageHolder:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        setSupportActionBar(toolbar_feedback)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        feedback_send.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    feedback()
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun CheckEditTextIsEmptyOrNot(){

        nameHolder = feedback_name.text.toString()
        emailHolder = feedback_email.text.toString()
        phoneHolder = feedback_mobile_number.text.toString()
        messageHolder = feedback_content.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(messageHolder)){
            CheckEditText = false
        } else {
            CheckEditText = true
        }
    }

    private fun feedback(){

        val stringRequest = object : StringRequest(Request.Method.POST, feedbackURL, object : Response.Listener<String>{
            override fun onResponse(response: String?) {
                Log.d("Debug", response)
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("Debug", error.toString())
            }
        }){
            override fun getParams():Map<String, String> {
                val params = HashMap<String, String>()
                params.put("name", nameHolder)
                params.put("email", emailHolder)
                params.put("mobilenumber", phoneHolder)
                Log.d("message", messageHolder)
                return params
            }
        }

        val requestVolley = Volley.newRequestQueue(this)
        requestVolley.add(stringRequest)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
