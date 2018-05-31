package my.com.itrain.big_car

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class PaymentActivity : AppCompatActivity() {

    var paymentURL = "https://gentle-atoll-11837.herokuapp.com/api/paymentmethods"
    private val paymenteMaterial = java.util.ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

//        setSupportActionBar(toolBarPayment)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val payAdapter = PayAdapter(applicationContext, object : PayAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent()
                intent.putExtra("selectedPayName", paymenteMaterial.get(position).getString("method"))
                intent.putExtra("selectedPayId", paymenteMaterial.get(position).getString("paymentmethod_id"))
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        val payLayoutManger = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycleViewPayment!!.layoutManager = payLayoutManger
        recycleViewPayment!!.itemAnimator = DefaultItemAnimator()
        recycleViewPayment!!.adapter = payAdapter

        val requestVolley = Volley.newRequestQueue(applicationContext)

        var jsonObjectRequestTour = JsonObjectRequest(Request.Method.GET, paymentURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val typeData = response.getJSONArray("data")

                for (i in 0 until typeData.length()){
                    paymenteMaterial.add(typeData.getJSONObject(i))
                    payAdapter.addJsonObject(typeData.getJSONObject(i))
                }
                payAdapter.notifyDataSetChanged()
            } catch (e : JSONException){
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequestTour)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId

        if (id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

class PayAdapter(private val context: Context, private val listener: PayAdapter.OnItemClickListener) : RecyclerView.Adapter<PayAdapter.ViewHolder>() {

    private val payment = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var payImage: ImageView
        var payTitle: TextView

        init {
            payImage = itemView.findViewById(R.id.paymentImage)
            payTitle = itemView.findViewById(R.id.paymentTitle)
        }

        fun bind(position: Int, listener: PayAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.multiple_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.payImage?.setImageResource(R.mipmap.ic_action_card)
        holder?.payTitle?.text = payment.get(position).getString("method")

        holder?.bind(position,listener)
    }

    override fun getItemCount(): Int {
        return payment.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        payment.add(jsonObject)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}
