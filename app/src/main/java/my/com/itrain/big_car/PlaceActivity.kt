package my.com.itrain.big_car

import android.app.ActionBar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_near_by.*
import kotlinx.android.synthetic.main.activity_place.*
import my.com.itrain.big_car.R.id.attractionRecyclerView
import org.json.JSONException
import org.json.JSONObject

class PlaceActivity : AppCompatActivity() {

    var attractionTourURL = "https://gentle-atoll-11837.herokuapp.com/api/tripnearby/"
    private val attractMaterial = java.util.ArrayList<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

//        setSupportActionBar(toolbarPlace)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        val AttractAdapter = AttractAdapter(this, object : AttractAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent()
                intent.putExtra("selectedDropName", attractMaterial.get(position).getString("attraction"))
                intent.putExtra("selectedDropId", attractMaterial.get(position).getString("attraction_id"))
                intent.putExtra("selectedLat", attractMaterial.get(position).getDouble("latitude"))
                intent.putExtra("selectedLng", attractMaterial.get(position).getDouble("longitude"))
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
        val nearbyLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        attractionRecyclerView!!.layoutManager = nearbyLayoutManager
        attractionRecyclerView!!.itemAnimator = DefaultItemAnimator()
        attractionRecyclerView!!.adapter = AttractAdapter

        //VOLLEY
        val requestVolley = Volley.newRequestQueue(this)

        val progressDialog = ProgressDialog(this, R.style.DialogTheme)
        progressDialog.setCancelable(false)
        progressDialog.isIndeterminate=true
        progressDialog.show()


        var jsonObjectRequest = JsonObjectRequest(Request.Method.GET,attractionTourURL+latitude+","+longitude,null, object : Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject) {
                try {

                    val attractionData = response.getJSONArray("data")
                    if (attractionData.length() == 0){

                        var attractionLayout = findViewById(R.id.attractionLayout) as RelativeLayout
                        val textView = TextView(applicationContext)
                        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                        textView.layoutParams = lp
                        textView.setText("No Destination Is Available")
                        textView.setTextColor(Color.parseColor("#FF0000"))
                        textView.setGravity(Gravity.CENTER)
                        attractionLayout.addView(textView)

                    }else {
                        for (i in 0 until attractionData.length()){
                            attractMaterial.add(attractionData.getJSONObject(i))
                            AttractAdapter.addJsonObject(attractionData.getJSONObject(i))
                        }
                        AttractAdapter.notifyDataSetChanged()
                    }
                    progressDialog.dismiss()
                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }
        },
                object : Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError) {
                        Log.d("Debug", error.toString())
                    }
                })

        requestVolley.add(jsonObjectRequest)
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

class AttractAdapter(private val context: Context, private val listener : AttractAdapter.OnItemClickListener):RecyclerView.Adapter<AttractAdapter.ViewHolder>() {

    private val attract = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var attractImage: ImageView
        var attractTitle: TextView
        var attractDesc: TextView

        init {
            attractImage = itemView.findViewById(R.id.attractImage)
            attractTitle = itemView.findViewById(R.id.attractTitle)
            attractDesc = itemView.findViewById(R.id.attractDesc)
        }

        fun bind(position: Int, listener: AttractAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.attraction_tour, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (attract.get(position).getString("attraction_image") == ""){
            holder?.attractImage?.setImageResource(R.drawable.no_available)
        } else {
            Picasso.with(context).load(attract.get(position).getString("attraction_image")).into(holder?.attractImage)
        }
        holder?.attractTitle?.text = attract.get(position).getString("attraction")
        holder?.attractDesc?.text = Html.fromHtml(attract.get(position).getString("admission_fee"))

        holder?.bind(position, listener)
    }

    override fun getItemCount(): Int {
        return attract.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        attract.add(jsonObject)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}
