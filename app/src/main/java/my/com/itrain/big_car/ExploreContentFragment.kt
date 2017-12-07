package my.com.itrain.big_car


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    lateinit var recylerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_explore_content, container, false)
        val list = ArrayList<Trend>()
        prepareList(list)

        recylerView = rootView.findViewById<RecyclerView>(R.id.recycleView) as RecyclerView
        //recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.adapter = ContentAdapter(this,list)

        return rootView
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend("Kanesh",R.drawable.happycutomer))
        list.add(Trend("Kan",R.drawable.happycutomer))
        list.add(Trend("Esh",R.drawable.happycutomer))
    }

}// Required empty public constructor

class Trend(val countryName : String, val countryImg : Int)
