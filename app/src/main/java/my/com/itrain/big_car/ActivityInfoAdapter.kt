package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject

class ActivityInfoAdapter(private val context: Context): RecyclerView.Adapter<ActivityInfoAdapter.ViewHolder>(){

    private val activityInfo = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tourInfoTitle: TextView
        var tourInfoDesc: TextView

        init {
            tourInfoTitle = itemView.findViewById(R.id.tourInfoTitle)
            tourInfoDesc = itemView.findViewById(R.id.tourInfoDesc)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.activity_information, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tourInfoTitle?.text = Html.fromHtml(activityInfo.get(position).getString("title"))
        holder?.tourInfoDesc?.text = Html.fromHtml(activityInfo.get(position).getString("desc"))
    }

    override fun getItemCount(): Int {
        return activityInfo.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        activityInfo.add(jsonObject)
    }
}