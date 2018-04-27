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
        var popDesc: TextView
        var popLocation: TextView

        init {
            popImg = itemView.findViewById(R.id.popularImg)
            popName = itemView.findViewById(R.id.popularName)
            popDesc = itemView.findViewById(R.id.popularDesc)
            popLocation = itemView.findViewById(R.id.popularLocation)
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
        //holder?.popImg?.setImageResource(R.drawable.tour2)
        Picasso.with(context).load(byCategory.get(position).getString("service_image")).into(holder?.popImg)
        holder?.popName?.text = Html.fromHtml(byCategory.get(position).getString("product_name"))
        holder?.popDesc?.text = Html.fromHtml(byCategory.get(position).getString("product_desc"))
        holder?.popLocation?.text = Html.fromHtml(byCategory.get(position).getString("location"))

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