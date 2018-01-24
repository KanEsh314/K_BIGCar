package my.com.itrain.big_car

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text

/**
 * Created by iTrain on 18-Jan-18.
 */

class CategoryAdapter(private val context : BrowseContentFragment, private val categoryAll : List<Categories>) : BaseAdapter() {

    //@SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.category_all_tour, parent, false)
        val categoryImage = view.findViewById<ImageView>(R.id.categoryImage)
        val categoryTitle = view.findViewById<TextView>(R.id.categoryTitle)
        val categoryTotal = view.findViewById<TextView>(R.id.totalCategory)

//        fun bind(position: Int, listener: OnItemClickListener){
//            view.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                    listener.onItemClick(position)
//                }
//            })
//        }

        val categories : Categories = categoryAll.get(position)
        categoryImage?.setImageResource(categories.catImg)
        categoryTitle?.text = categories.catTitle
        categoryTotal?.text = categories.catTotal
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

//    interface OnItemClickListener{
//        fun onItemClick(position:Int)
//    }

}