package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text

/**
 * Created by iTrain on 20-Dec-17.
 */

class ExplorePopularContent(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<ExplorePopularContent.ViewHolder>(){

    private val byCategory = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var popImg: ImageView
        var popName: TextView
        var popRating: RatingBar
        var popRatingText: TextView
        var popLocation: TextView
        var popPrice: TextView

        init {
            popImg = itemView.findViewById(R.id.popularImg)
            popName = itemView.findViewById(R.id.popularName)
            popRating = itemView.findViewById(R.id.popularRating)
            popRatingText = itemView.findViewById(R.id.popularRatingText)
            popLocation = itemView.findViewById(R.id.popularLocation)
            popPrice = itemView.findViewById(R.id.popularPrice)
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

        if (byCategory.get(position).getString("grid_image") == ""){
            holder?.popImg?.setImageResource(R.drawable.no_available)
        }else {
            Picasso.with(context).load(byCategory.get(position).getString("grid_image")).into(holder?.popImg)
        }
        holder?.popName?.text = Html.fromHtml(byCategory.get(position).getString("product_name"))
        holder?.popRating?.rating = byCategory.get(position).getString("total_rating").toFloat()
        holder?.popRatingText?.text = "(" + byCategory.get(position).getString("total_review") + ")"
        holder?.popLocation?.text = Html.fromHtml(byCategory.get(position).getString("short_prod_code"))
        if (byCategory.get(position).getString("lowest_price") == ""){
        } else {
            holder?.popPrice?.text = "From RM" + byCategory.get(position).getString("lowest_price")
        }

        holder?.bind(position,listener)

    }

    override fun getItemCount(): Int {
        return byCategory.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject: JSONObject) {
        byCategory.add(jsonObject)
    }
}