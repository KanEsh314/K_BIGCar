package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import org.json.JSONObject

class SelectCountAdapter(private val context: Context): RecyclerView.Adapter<SelectCountAdapter.ViewHolder>(){

    private val tourPackage = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tourCategories: TextView
        var tourCategoriesPrice: TextView
        var tourCount : ElegantNumberButton

        init {
            tourCategories = itemView.findViewById(R.id.countCategories)
            tourCategoriesPrice = itemView.findViewById(R.id.countPrice)
            tourCount = itemView.findViewById(R.id.count)
            tourCount.setOnClickListener(object : ElegantNumberButton.OnClickListener{
                override fun onClick(view: View?) {
                    Log.d("Debug", tourCount.toString())
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tourselect_count, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
//        holder?.tourCategories?.text = setcount.selectTourCat
//        holder?.tourCategoriesPrice?.text = setcount.selectTourCatPrice
        holder?.tourCount?.setRange(1, 3)
        holder?.tourCategories?.text = "Passenger"
                //Log.d("Debug" ,tourPackage.get(position).getString("package_name"))
        holder?.tourCategoriesPrice?.text = "RM 12"
                //tourPackage.get(position).getString("package_price")
    }

    override fun getItemCount(): Int {
        return 3
    }

    fun addJsonObject(jsonObject: JSONObject) {
        tourPackage.add(jsonObject)
    }
}