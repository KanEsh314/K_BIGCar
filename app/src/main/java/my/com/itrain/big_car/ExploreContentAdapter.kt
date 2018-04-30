package my.com.itrain.big_car

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.json.JSONObject

/**
 * Created by iTrain on 13-Dec-17.
 */
class ExploreContentAdapter(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<ExploreContentAdapter.ViewHolder>() {

    private val popular = ArrayList<JSONObject>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trendImg: ImageView
        var trendText: TextView
        var trendPrice: TextView

        init {
            trendImg = itemView.findViewById(R.id.trendImg)
            trendText = itemView.findViewById(R.id.trendText)
            trendPrice = itemView.findViewById(R.id.trendPrice)
        }

        fun bind(position: Int, listener: ExploreContentAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        Picasso.with(context).load(popular.get(position).getString("image")).into(holder?.trendImg)
        holder?.trendText?.text = popular.get(position).getString("product_name")
        holder?.trendPrice?.text = popular.get(position).getString("short_prod_code")

        holder?.bind(position,listener)
    }

    override fun getItemCount(): Int {
        return popular.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        popular.add(jsonObject)
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}