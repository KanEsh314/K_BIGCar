package my.com.itrain.big_car

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_all_tour.view.*
import kotlinx.android.synthetic.main.fragment_browse_content.*
import my.com.itrain.big_car.R.id.categoryImage
import org.json.JSONObject
import org.w3c.dom.Text

/**
 * Created by iTrain on 18-Jan-18.
 */

class CategoryAdapter(private val context: Context, private val listener: CategoryAdapter.OnItemClickListener) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val categoryAll = ArrayList<JSONObject>()

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView
        var categoryImage: ImageView

        init {
            categoryTitle = itemView.findViewById(R.id.categoryText)
            categoryImage = itemView.findViewById(R.id.categoryImage)
        }

        fun bind(position: Int, listener: CategoryAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.category_all_tour,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryAll.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        if (categoryAll.get(position).getString("servicecat_image") == ""){
            holder?.categoryImage?.setImageResource(R.drawable.no_available)
        }else{
            Picasso.with(context).load(categoryAll.get(position).getString("servicecat_image")).into(holder?.categoryImage)
        }
        holder?.categoryTitle?.text = categoryAll.get(position).getString("servicecat_name")

        holder?.bind(position,listener)
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject: JSONObject) {
        categoryAll.add(jsonObject)
    }
}
