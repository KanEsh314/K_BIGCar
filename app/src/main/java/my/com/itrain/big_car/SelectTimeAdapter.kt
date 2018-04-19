package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import my.com.itrain.big_car.R.id.tourTime
import org.json.JSONObject
import org.w3c.dom.Text

/**
 * Created by iTrain on 10-Jan-18.
 */

class SelectTimeAdapter(private val context: Context):RecyclerView.Adapter<SelectTimeAdapter.ViewHolder>(){

    private val tourTime = ArrayList<JSONObject>()
    private var selectedPosition = -1

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tourText: TextView
            var tourTime: RadioButton

        init {
            tourText = itemView.findViewById(R.id.tourSelectText)
            tourTime = itemView.findViewById(R.id.tourSelectTime)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tourselect_time, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tourText?.text = tourTime.get(position).getString("time")

        holder?.tourTime?.setChecked(position === selectedPosition)
        holder?.tourTime?.setTag(position)
        holder?.tourTime?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                itemCheckChanged(v)
            }
        })

    }

    private fun itemCheckChanged(v: View?) {
        selectedPosition = v?.getTag() as Int
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tourTime.size
    }

//    fun getSelectedItem():String {
//        if (selectedPosition !== -1)
//        {
//            Toast.makeText(context, "Selected Item : " + tourTime.get(selectedPosition), Toast.LENGTH_SHORT).show()
//            return tourTime.get(selectedPosition)
//        }
//        return ""
//    }

    fun addJsonObject(jsonObject: JSONObject) {
        tourTime.add(jsonObject)
    }
}