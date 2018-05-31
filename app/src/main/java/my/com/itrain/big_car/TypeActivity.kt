package my.com.itrain.big_car

import android.app.Activity
import android.app.ProgressDialog
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
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_type.*
import org.json.JSONException
import org.json.JSONObject

class TypeActivity : AppCompatActivity() {

    var typeURL = "https://gentle-atoll-11837.herokuapp.com/api/triptypes"
    private val typeMaterial = java.util.ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

//        setSupportActionBar(toolBarType)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        val typeAdapter = TypeAdapter(applicationContext, object : TypeAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent()
                intent.putExtra("selectedTypeName", typeMaterial.get(position).getString("type"))
                intent.putExtra("selectedTypeId", typeMaterial.get(position).getString("triptype_id"))
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        val typeLayoutManger = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        recycleViewType!!.layoutManager = typeLayoutManger
        recycleViewType!!.itemAnimator = DefaultItemAnimator()
        recycleViewType!!.adapter = typeAdapter

        val requestVolley = Volley.newRequestQueue(applicationContext)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()

        var jsonObjectRequestTour = JsonObjectRequest(Request.Method.GET, typeURL,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) = try {

                val typeData = response.getJSONArray("data")

                for (i in 0 until typeData.length()){
                    typeMaterial.add(typeData.getJSONObject(i))
                    typeAdapter.addJsonObject(typeData.getJSONObject(i))
                }
                progressDialog.dismiss()
                typeAdapter.notifyDataSetChanged()
            } catch (e : JSONException){
                progressDialog.dismiss()
                e.printStackTrace()
            }
        },
                object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError) {
                        progressDialog.dismiss()
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

class TypeAdapter(private val context: Context, private val listener: TypeAdapter.OnItemClickListener): RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    private val type = ArrayList<JSONObject>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeImage: ImageView
        var typeTitle: TextView

        init {
            typeImage = itemView.findViewById(R.id.typeImage)
            typeTitle = itemView.findViewById(R.id.typeTitle)
        }

        fun bind(position: Int, listener: TypeAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.multiple_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.typeImage?.setImageResource(R.mipmap.ic_action_personal)
        holder?.typeTitle?.text = type.get(position).getString("type")

        holder?.bind(position,listener)
    }

    override fun getItemCount(): Int {
        return type.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        type.add(jsonObject)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}
