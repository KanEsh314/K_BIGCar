package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

/**
 * Created by iTrain on 05-Jan-18.
 */

class PackageAdapter(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    private val packageOption = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var packageName: TextView
        var packagePrice: TextView
        //var packagePax: TextView
        var packageButton: Button

        init {
            packageName = itemView.findViewById(R.id.packageTitle)
            packagePrice = itemView.findViewById(R.id.packagePrice)
            //packagePax = itemView.findViewById(R.id.packagePax)
            packageButton = itemView.findViewById(R.id.chooseDate)
        }

        fun bind(position: Int, listener: OnItemClickListener){
            packageButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.choose_package,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.packageName?.text = packageOption.get(position).getString("package_name")
        //holder?.packagePax?.text = packageOption.get(position).getString("package_pax")
        holder?.packagePrice?.text = packageOption.get(position).getString("package_price")

        holder?.bind(position, listener)
    }

    override fun getItemCount(): Int {
        return packageOption.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject: JSONObject) {
        packageOption.add(jsonObject)
    }
}