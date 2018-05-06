package my.com.itrain.big_car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class SummaryAdapter(private val context: Context) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>(){

    private val bookingDetail = ArrayList<String>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var summaryPassengerName : TextView
        var summaryIcPassport : TextView

        init {
            summaryPassengerName = itemView.findViewById(R.id.summary_name_passenger)
            summaryIcPassport = itemView.findViewById(R.id.summary_ic_number)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.passenger_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.summaryPassengerName?.text
        holder?.summaryIcPassport?.text
    }

    override fun getItemCount(): Int{
        return bookingDetail.size
    }

    fun addBookingPassenger(booked_passenger: String) {
        bookingDetail.add(booked_passenger)
    }


}