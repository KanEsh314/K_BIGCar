package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import my.com.itrain.big_car.R.id.bookingImageView
import org.json.JSONObject

class BookingContent (private val context: Context, private val listener: BookingContent.OnItemClickListener) : RecyclerView.Adapter<BookingContent.ViewHolder>(){

    private val myBooking = ArrayList<JSONObject>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var bookingImageView: ImageView
        var bokingPlaceName: TextView
        var bookingPlace: TextView

        init {
            bookingImageView = itemView.findViewById(R.id.bookingImageView)
            bokingPlaceName = itemView.findViewById(R.id.bookingName)
            bookingPlace = itemView.findViewById(R.id.bookingPlace)
        }

        fun bind(position: Int, listener: BookingContent.OnItemClickListener){
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.booking_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        Picasso.with(context).load(myBooking.get(position).getString("image")).into(holder?.bookingImageView)
        holder?.bokingPlaceName?.text = Html.fromHtml(myBooking.get(position).getString("product_name"))
        holder?.bookingPlace?.text = Html.fromHtml(myBooking.get(position).getString("short_prod_code"))

        holder?.bind(position,listener)
    }


    override fun getItemCount(): Int {
        return myBooking.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    fun addJsonObject(jsonObject: JSONObject) {
        myBooking.add(jsonObject)
    }

}