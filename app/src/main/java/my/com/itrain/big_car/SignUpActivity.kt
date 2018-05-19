package my.com.itrain.big_car

import android.app.Activity
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
import android.graphics.Bitmap;
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.util.HashMap

class SignUpActivity : AppCompatActivity() {

    var CheckEditText:Boolean = false
    var registerURL = "http://gentle-atoll-11837.herokuapp.com/api/registeruser"

    var profileHolder: String = ""
    var icpassportHolder: String = ""
    var nameHolder: String = ""
    var emailHolder: String = ""
    var addressHolder: String = ""
    var phoneHolder: String = ""
    var passwordHolder: String = ""
    lateinit var bitmap:Bitmap

    val PICK_IMAGE_REQUEST = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        back_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        register_image.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_PICK)
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
            }
        })

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode === PICK_IMAGE_REQUEST && resultCode === RESULT_OK && data != null && data.getData() != null)
            {
                val filePath = data.data
                try
                {
                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    //Setting image to ImageView
                    register_image.setImageBitmap(bitmap)
                    //
                    Log.d("Image", bitmap.toString())
                }
                catch (e:Exception) {
                    Log.d("Debug", e.toString())
                    e.printStackTrace()
                }
            }
        }


        registerBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                CheckEditTextIsEmptyOrNot()
                if (CheckEditText){
                    userImage()
                    userRegistration()
                    try{

                    }catch (e : JSONException){

                    }
                }else{
                       Toast.makeText(applicationContext, "Please fill all form fields.", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun userImage() {

    }

    private fun CheckEditTextIsEmptyOrNot() {

        nameHolder = register_name.text.toString()
        icpassportHolder = register_ic_passport.toString()
        emailHolder = register_email.text.toString()
        addressHolder = register_address.text.toString()
        phoneHolder = register_phonenumber.text.toString()
        passwordHolder = register_password.text.toString()

        if (TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(icpassportHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(addressHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(passwordHolder)){
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

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                var imageBytes = baos.toByteArray()
                //profileHolder =

                params.put("profilepic", Base64.encodeToString(imageBytes, Base64.DEFAULT))
                params.put("name", nameHolder)
                params.put("ic", icpassportHolder)
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
