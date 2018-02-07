package my.com.itrain.big_car

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * Created by iTrain on 06-Feb-18.
 */

class NotifyAdapter(private val context: InboxContentFragment, private val notifyAll : List<Notity>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.notify_activity, parent, false)
        val notifyTitle = view.findViewById<TextView>(R.id.notifyTitle)
        val notifySub = view.findViewById<TextView>(R.id.notifySubTitle)
        val notifyDesc = view.findViewById<TextView>(R.id.notifyDesc)

        val notify : Notity = notifyAll.get(position)
        notifyTitle?.text = notify.notTitle
        notifySub?.text = notify.notSub
        notifyDesc?.text = notify.notDesc
        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notifyAll.size
    }


}