package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import org.w3c.dom.Text

/**
 * Created by iTrain on 20-Dec-17.
 */

class ExplorePopularContent(private val content: ExploreContentFragment, private val trending: List<Popular>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ExplorePopularContent.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var popImg: ImageView
        var popName: TextView
        var popRating: RatingBar
        var popRatingText: TextView
        var popPrice: TextView
        var popAval: TextView

        init {
            popImg = itemView.findViewById(R.id.popularImg)
            popName = itemView.findViewById(R.id.popularName)
            popRating = itemView.findViewById(R.id.popularStar)
            popRatingText = itemView.findViewById(R.id.popularStarText)
            popPrice = itemView.findViewById(R.id.popularPrice)
            popAval = itemView.findViewById(R.id.popularAvalability)
        }

        fun bind(position: Int, listener: ExplorePopularContent.OnItemClickListener){
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_trending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val popular : Popular = trending.get(position)
        holder?.popImg?.setImageResource(popular.popularimg)
        holder?.popName?.text = popular.popularname
        holder?.popRating?.rating = popular.popularrating
        holder?.popRatingText?.text = popular.popularratingtext
        holder?.popPrice?.text = popular.popularprice
        holder?.popAval?.text = popular.popularavalability

        holder?.bind(position,listener)

    }

    override fun getItemCount(): Int {
        return trending.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}