package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by iTrain on 20-Dec-17.
 */

class ExplorePopularContent(private val content: ExploreContentFragment, private val trending: List<Popular>) : RecyclerView.Adapter<ExplorePopularContent.ViewHolder>(){
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val popular : Popular = trending.get(position)
        holder?.popImg?.setImageResource(popular.popularimg)
        holder?.popName?.text = popular.popularname
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var popImg: ImageView
        var popName: TextView

        init {
            popImg = itemView.findViewById(R.id.popularImg)
            popName = itemView.findViewById(R.id.popularName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_trending, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return trending.size
    }
}