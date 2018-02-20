package my.com.itrain.big_car

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by iTrain on 19-Dec-17.
 */

class ExplorePlaceContentAdapter(private val context: Context,private val listener: OnItemClickListener) : RecyclerView.Adapter<ExplorePlaceContentAdapter.ViewHolder>(){

    private val destination = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var placeImg: ImageView
        var placeName: TextView

        init {
            placeImg = itemView.findViewById(R.id.placeImg)
            placeName = itemView.findViewById(R.id.placeName)
        }

        fun bind(position: Int, listener: ExplorePlaceContentAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_placecontent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        //Log.d("Debug",destination.get(position).getString("service_image"))
        //Picasso.with(context).load(destination.get(position).getString("service_image")).into(holder?.placeImg)
        holder?.placeImg?.setImageResource(R.drawable.tour1)
        holder?.placeName?.text = destination.get(position).getString("product_name")

        holder?.bind(position,listener)

    }

    override fun getItemCount(): Int {
        return destination.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject:JSONObject) {
        destination.add(jsonObject)

    }

}
