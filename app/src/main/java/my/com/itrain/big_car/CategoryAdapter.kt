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

class CategoryAdapter(private val context : BrowseContentFragment, private val tourCategory: List<AllTour>, private val listener: OnItemClickListener) : BaseAdapter() {

    val categoryImageView = findViewById<ImageView>(R.id.categoryImage)
    val categoryTitle = findViewById<TextView>(R.id.categoryTitle)
    val categoryTotal = findViewById<TextView>(R.id.totalCategory)
    val ViewHolder(itemView: View)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent?.context).inflate(R.layout.category_all_tour, parent, false)
        return layoutInflater
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