package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton

/**
 * Created by iTrain on 10-Jan-18.
 */
class SelectCountAdapter(private val content: TourCountActivity, private val tourcount: List<SetCount>): RecyclerView.Adapter<SelectCountAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tourCategories: TextView
        var tourCategoriesPrice: TextView
        var tourCount : ElegantNumberButton

        init {
            tourCategories = itemView.findViewById(R.id.countCategories)
            tourCategoriesPrice = itemView.findViewById(R.id.countPrice)
            tourCount = itemView.findViewById(R.id.count)
            tourCount.setRange(0,8)
            tourCount.setOnClickListener(object : ElegantNumberButton.OnClickListener{
                override fun onClick(view: View?) {
                    //Working
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tourselect_count, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val setcount : SetCount = tourcount.get(position)
        holder?.tourCategories?.text = setcount.selectTourCat
        holder?.tourCategoriesPrice?.text = setcount.selectTourCatPrice
    }

    override fun getItemCount(): Int {
        return tourcount.size
    }
}