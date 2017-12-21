package my.com.itrain.big_car

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

/**
 * Created by iTrain on 13-Dec-17.
 */
class ExploreContentAdapter(private val context: ExploreContentFragment, private val list: List<Trend>) : RecyclerView.Adapter<ExploreContentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trendImg: ImageView
        var trendText: TextView
        var trendTextEla: TextView
        var trendDesc: TextView
        var trendPrice: TextView

        init {
            trendImg = itemView.findViewById(R.id.trendImg)
            trendText = itemView.findViewById(R.id.trendText)
            trendTextEla = itemView.findViewById(R.id.trendTextEla)
            trendDesc = itemView.findViewById(R.id.trendDesc)
            trendPrice = itemView.findViewById(R.id.trendPrice)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val trend : Trend = list.get(position)
        holder?.trendImg?.setImageResource(trend.img)
        holder?.trendText?.text = trend.text
        holder?.trendTextEla?.text = trend.textela
        holder?.trendDesc?.text = trend.desc
        holder?.trendPrice?.text = trend.price
    }

    override fun getItemCount(): Int {
        return list.size
    }
}