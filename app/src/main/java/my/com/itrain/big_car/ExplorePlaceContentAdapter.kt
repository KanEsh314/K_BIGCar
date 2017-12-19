package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by iTrain on 19-Dec-17.
 */

class ExplorePlaceContentAdapter(private val context: ExploreContentFragment, private val loc: List<Place>) : RecyclerView.Adapter<ExplorePlaceContentAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var placeImg: ImageView
        var placeName: TextView

        init {
            placeImg = itemView.findViewById(R.id.placeImg)
            placeName = itemView.findViewById(R.id.placeName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.explore_placecontent, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val place : Place = loc.get(position)
        holder?.placeImg?.setImageResource(place.pimg)
        holder?.placeName?.text = place.pname
    }

    override fun getItemCount(): Int {
        return loc.size
    }


}