package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by iTrain on 06-Dec-17.
 */

class ContentAdapter(private val context: ExploreContentFragment, private val list: List<Trend>) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var trendTitle : TextView
        var trendImg : ImageView

        init {
            trendTitle = itemView.findViewById(R.id.trendText)
            trendImg = itemView.findViewById(R.id.trendImg)
        }
    }

    override fun onBindViewHolder(holder: ContentAdapter.ViewHolder?, position: Int) {
        var trend : Trend = list.get(position)
        holder!!.trendTitle!!.text = trend.countryName
        holder!!.trendImg!!.setImageResource(trend.countryImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContentAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.explore_list, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }
}