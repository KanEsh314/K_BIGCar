package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by iTrain on 18-Jan-18.
 */

class CategoryAdapter(private val context : BrowseContentFragment, private val listener: OnItemClickListener) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.category_all_tour, parent, false)

        val categoryImage = view.findViewById<ImageView>(R.id.categoryImage)
        val categoryTitle = view.findViewById<TextView>(R.id.categoryTitle)
        val categoryTotal = view.findViewById<TextView>(R.id.totalCategory)

//        val ViewHolder = ViewHolder(categoryImage, categoryTitle, categoryTotal)

        return view
    }

    override fun getItem(position: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

}