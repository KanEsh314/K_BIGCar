package my.com.itrain.big_car


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
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
        return inflater!!.inflate(R.layout.explore_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<Trend>()
        prepareList(list)
        val mAdapter = ExploreContentAdapter(this,list)
        val mLayoutManager = GridLayoutManager(this.activity,3)
        recycleViewPopular!!.layoutManager = mLayoutManager
        recycleViewPopular!!.itemAnimator = DefaultItemAnimator()
        recycleViewPopular!!.adapter = mAdapter

        val loc = ArrayList<Place>()
        preparePlace(loc)
        val pAdapter = ExplorePlaceContentAdapter(this,loc)
        val pLayoutManager = LinearLayoutManager(this.activity,LinearLayout.HORIZONTAL,true)
        recycleViewDestination!!.layoutManager = pLayoutManager
        recycleViewDestination!!.itemAnimator = DefaultItemAnimator()
        recycleViewDestination!!.adapter = pAdapter
    }

    private fun preparePlace(loc: ArrayList<Place>) {
        loc.add(Place(R.drawable.tour1,"Tokyo"))
        loc.add(Place(R.drawable.tour2,"Singapore"))
        loc.add(Place(R.drawable.tour2,"USA"))
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.tour1,"MYR198","After","Crime Histroy", "19 Reviews"))
        list.add(Trend(R.drawable.tour2,"MYR198","After","Crime Histroy", "19 Reviews"))
        list.add(Trend(R.drawable.tour1,"MYR198","After","Crime Histroy", "19 Reviews"))
        list.add(Trend(R.drawable.tour2,"MYR198","After","Crime Histroy", "19 Reviews"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val textela : String, val desc : String, val price : String)

class Place(val pimg : Int, val pname : String)
