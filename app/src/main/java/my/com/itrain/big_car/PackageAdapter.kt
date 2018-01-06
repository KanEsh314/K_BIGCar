package my.com.itrain.big_car

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by iTrain on 05-Jan-18.
 */

class PackageAdapter(private val context: TourDatesActivity, private val packageOption: List<PackageTour>, private val listener: OnItemClickListener) : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var packageName: TextView
        var packagePrice: TextView

        init {
            packageName = itemView.findViewById(R.id.packageTitle)
            packagePrice = itemView.findViewById(R.id.packagePrice)
        }

        fun bind(position: Int, listener: OnItemClickListener){
            itemView.setOnClickListener(object : View.OnClickListener {
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
        val packageOption : PackageTour = packageOption.get(position)
        holder?.packageName?.text = packageOption.packageName
        holder?.packagePrice?.text = packageOption.packagePrice

        holder?.bind(position, listener)
    }

    override fun getItemCount(): Int {
        return packageOption.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}