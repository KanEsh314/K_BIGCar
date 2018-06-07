package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text

class NearByAdapter(private val context: Context, private val listener: NearByAdapter.OnItemClickListener): RecyclerView.Adapter<NearByAdapter.ViewHolder>(){

    private val nearBy = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nearImg : ImageView
        var nearTitle : TextView
        var nearRating : RatingBar
        var nearRatingText : TextView
        var nearPrice : TextView
        var nearLocation : TextView


        init {
            nearImg = itemView.findViewById(R.id.nearByImg)
            nearTitle = itemView.findViewById(R.id.nearByName)
            nearRating = itemView.findViewById(R.id.nearByRating)
            nearRatingText = itemView.findViewById(R.id.nearByRatingText)
            nearPrice = itemView.findViewById(R.id.nearByPrice)
            nearLocation = itemView.findViewById(R.id.nearByLocation)
        }

        fun bind(position: Int, listener: NearByAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.nearby_tour, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        Picasso.with(context).load(nearBy.get(position).getString("grid_image")).into(holder?.nearImg)
        holder?.nearTitle?.text = nearBy.get(position).getString("product_name")
        holder?.nearRating?.rating = nearBy.get(position).getString("total_rating").toFloat()
        holder?.nearRatingText?.text = "(" + nearBy.get(position).getString("total_review") + ")"
        holder?.nearLocation?.text = nearBy.get(position).getString("short_prod_code")
        if (nearBy.get(position).getString("lowest_price") == ""){
        } else {
            holder?.nearPrice?.text = "From RM" + nearBy.get(position).getString("lowest_price")
        }

        holder?.bind(position, listener)
    }

    override fun getItemCount(): Int {
        return nearBy.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        nearBy.add(jsonObject)
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}