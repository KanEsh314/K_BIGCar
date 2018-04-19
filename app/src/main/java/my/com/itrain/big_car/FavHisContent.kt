package my.com.itrain.big_car;

import android.content.Context
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class FavHisContent(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<FavHisContent.ViewHolder>(){

    private val byCategory = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var favImageView:ImageView
        var favName:TextView
        var favPrice: TextView

        init {
            favImageView = itemView.findViewById(R.id.favImageView)
            favName = itemView.findViewById(R.id.favName)
            favPrice = itemView.findViewById(R.id.favPrice)
        }

        fun bind(position: Int, listener: FavHisContent.OnItemClickListener){
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.favorite_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.favImageView?.setImageResource(R.drawable.tour2)
        holder?.favName?.text = Html.fromHtml(byCategory.get(position).getString("product_name"))
        holder?.favPrice?.text = Html.fromHtml(byCategory.get(position).getString("product_desc"))

        holder?.bind(position,listener)

    }

    override fun getItemCount(): Int {
        return byCategory.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject:JSONObject) {
        byCategory.add(jsonObject)
    }

}
