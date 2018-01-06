package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

/**
 * Created by iTrain on 05-Jan-18.
 */

class ReviewContentAdapter(private val context: Context, private val userreview: List<Review>) : RecyclerView.Adapter<ReviewContentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userPic: ImageView
        var userName: TextView
        var reviewRatingStar: RatingBar
        var reviewRatingText: TextView
        var userReview: TextView

        init {
            userPic = itemView.findViewById(R.id.userPicture)
            userName = itemView.findViewById(R.id.rateUsername)
            reviewRatingStar = itemView.findViewById(R.id.ratingGiven)
            reviewRatingText = itemView.findViewById(R.id.ratingGivenTime)
            userReview = itemView.findViewById(R.id.userReview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReviewContentAdapter.ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.tour_review, parent, false)
        return ReviewContentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val review : Review = userreview.get(position)
        holder?.userPic?.setImageResource(review.userPic)
        holder?.userName?.text = review.reviewName
        holder?.reviewRatingStar?.rating = review.ratingBarStar
        holder?.reviewRatingText?.text = review.ratingBarText
        holder?.userReview?.text = review.userReview
    }

    override fun getItemCount(): Int {
        return userreview.size
    }
}
