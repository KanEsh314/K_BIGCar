package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text

class NearByAdapter(private val context: Context, private val listener: NearByAdapter.OnItemClickListener): RecyclerView.Adapter<NearByAdapter.ViewHolder>(){

    private val nearBy = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nearImg : ImageView
        var nearTitle : TextView
        var nearDesc : TextView
        var nearLocation : TextView

        init {
            nearImg = itemView.findViewById(R.id.nearByImg)
            nearTitle = itemView.findViewById(R.id.nearByName)
            nearDesc = itemView.findViewById(R.id.nearByDesc)
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
        Picasso.with(context).load(nearBy.get(position).getString("grid_imagef")).into(holder?.nearImg)
        holder?.nearTitle?.text = nearBy.get(position).getString("product_name")
        holder?.nearDesc?.text = nearBy.get(position).getString("short_desc")
        holder?.nearLocation?.text = nearBy.get(position).getString("short_prod_code")

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