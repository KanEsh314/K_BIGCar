package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import org.json.JSONObject

/**
 * Created by iTrain on 05-Jan-18.
 */

class ReviewContentAdapter(private val context: Context) : RecyclerView.Adapter<ReviewContentAdapter.ViewHolder>() {

    private val userreview = ArrayList<JSONObject>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userPic: ImageView
        var userName: TextView
        var reviewRatingStar: RatingBar
        var userReview: TextView

        init {
            userPic = itemView.findViewById(R.id.userPicture)
            userName = itemView.findViewById(R.id.rateUsername)
            reviewRatingStar = itemView.findViewById(R.id.ratingGiven)
            userReview = itemView.findViewById(R.id.userReview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReviewContentAdapter.ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tour_review, parent, false)
        return ReviewContentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.userPic?.setImageResource(R.mipmap.ic_app_user)
        holder?.userName?.text = userreview.get(position).getString("user_id")
        holder?.reviewRatingStar?.rating = userreview.get(position).getInt("rating").toFloat()
        holder?.userReview?.text = userreview.get(position).getString("user_comment")
    }

    override fun getItemCount(): Int {
        return userreview.size
    }

    fun addJsonObject(jsonObject: JSONObject) {
        userreview.add(jsonObject)
    }
}
