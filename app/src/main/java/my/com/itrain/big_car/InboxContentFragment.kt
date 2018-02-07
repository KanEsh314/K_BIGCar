package my.com.itrain.big_car


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_inbox_content.*


/**
 * A simple [Fragment] subclass.
 */
class InboxContentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_content, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notify = ArrayList<Notity>()
        prepareNotify(notify)
        val notifyAdapter = NotifyAdapter(this, notify)
        listViewNotify!!.adapter = notifyAdapter
    }

    private fun prepareNotify(notify: ArrayList<Notity>) {
        notify.add(Notity("10 JAN 2018","USA Trip 20% OFF","Lorem Ipsum began as scrambled, nonsensical Latin derived from Cicero's 1st-century BC text De Finibus Bonorum et Malorum."))
        notify.add(Notity("05 JAN 2018","15% OFF on BIGCAR","Lorem Ipsum began as scrambled, nonsensical Latin derived from Cicero's 1st-century BC text De Finibus Bonorum et Malorum."))
        notify.add(Notity("12 DEC 2018","Redeem Now Airport Trips","Lorem Ipsum began as scrambled, nonsensical Latin derived from Cicero's 1st-century BC text De Finibus Bonorum et Malorum."))
    }
}// Required empty public constructor

class Notity(val notTitle : String, val notSub: String, val notDesc: String)