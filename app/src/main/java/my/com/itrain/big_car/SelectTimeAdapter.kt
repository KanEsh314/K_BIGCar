package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView

/**
 * Created by iTrain on 10-Jan-18.
 */

class SelectTimeAdapter(private val content: TourCountActivity, private val tourtime: List<SetTime>):RecyclerView.Adapter<SelectTimeAdapter.ViewHolder>(){

    private val mSelectedPosition = -1

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tourTime: RadioButton

        init {
            tourTime = itemView.findViewById(R.id.tourSelectTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tourselect_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val settime : SetTime = tourtime.get(position)
        holder?.tourTime?.text = settime.selectTourTime
        holder?.tourTime?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                holder?.tourTime?.isChecked = true
            }
        })
    }

    override fun getItemCount(): Int {
        return tourtime.size
    }
}