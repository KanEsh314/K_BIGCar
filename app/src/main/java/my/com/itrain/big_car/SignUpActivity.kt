package my.com.itrain.big_car

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_tour_confirm.*
import java.util.HashMap

class SignUpActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var registerURL = "http://gentle-atoll-11837.herokuapp.com/api/registeruser"

    var nameHolder:String = ""
    var emailHolder:String = ""
    var addressHolder:String = ""
    var phoneHolder:String = ""
    var passwordHolder:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        back_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        registerBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    userRegistration()
                }else{
                       Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun CheckEditTextIsEmptyOrNot() {

        nameHolder = register_name.text.toString()
        emailHolder = register_email.text.toString()
        addressHolder = register_address.text.toString()
        phoneHolder = register_phonenumber.text.toString()
        passwordHolder = register_password.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(addressHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(passwordHolder)){
            CheckEditText = false
        } else {
            CheckEditText = true
        }
    }

    private fun userRegistration() {

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server")
        progressDialog.show()

        val stringRequest = object : StringRequest(Request.Method.POST, registerURL, object : Response.Listener<String> {
            override fun onResponse(response: String?) {
                progressDialog.dismiss()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                //Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            }

        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        progressDialog.dismiss()
                        //Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                    }

                }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("name", nameHolder)
                params.put("email", emailHolder)
                params.put("address", emailHolder)
                params.put("phonenumber", phoneHolder)
                params.put("password", passwordHolder)
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
