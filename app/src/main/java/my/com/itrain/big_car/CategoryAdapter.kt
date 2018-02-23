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
import my.com.itrain.big_car.R.id.listViewCategory
import org.json.JSONObject
import org.w3c.dom.Text

/**
 * Created by iTrain on 18-Jan-18.
 */
//, private val listener : OnItemClickListener
class CategoryAdapter(private val context: Context) : BaseAdapter() {

    private val categoryAll = ArrayList<JSONObject>()

    //@SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.category_all_tour, parent, false)
        val categoryImage = view.findViewById<ImageView>(R.id.categoryImage)
        val categoryTitle = view.findViewById<TextView>(R.id.categoryTitle)

//        fun bind(position: Int, listener: OnItemClickListener){
//            view.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                    Log.d("Working","Fine")
//                    listener.onItemClick(position)
//                }
//            })
//        }

        Picasso.with(context).load(categoryAll.get(position).getString("servicecat_image")).into(categoryImage)
        //categoryImage?.setImageResource(R.drawable.tour1)
        categoryTitle?.text = categoryAll.get(position).getString("servicecat_name")
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categoryAll.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        categoryAll.add(jsonObject)
    }

//    interface OnItemClickListener{
//        fun onItemClick(position:Int)
//    }

}
