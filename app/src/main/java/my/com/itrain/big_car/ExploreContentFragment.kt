package my.com.itrain.big_car


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_explore_content.*
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_explore_content, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<Trend>()
        prepareList(list)
        val mAdapter = ExploreContentAdapter(this,list)
        val mLayoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL, false)
        recycleView!!.layoutManager = mLayoutManager
        recycleView!!.itemAnimator = DefaultItemAnimator()
        recycleView!!.adapter = mAdapter
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.photo,"Kan","Kan","Kan"))
        list.add(Trend(R.drawable.photo,"Esh","Esh","Esh"))
        list.add(Trend(R.drawable.photo,"Kan","Kan","Kan"))
        list.add(Trend(R.drawable.photo,"Esh","Esh","Esh"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val desc : String, val price : String)
