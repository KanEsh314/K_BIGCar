package my.com.itrain.big_car


import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import kotlinx.android.synthetic.main.fragment_explore_content.*
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore_content, container, false)


        var newHeight = (Resources.getSystem().displayMetrics.heightPixels)/2
        var bannerImage = view.findViewById<ImageView>(R.id.tourbannerImage)
        bannerImage.requestLayout()
        bannerImage.layoutParams.height = newHeight

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<Trend>()
        prepareList(list)
        val popularAdapter = ExploreContentAdapter(this,list)
        val popularLayoutManager = GridLayoutManager(this.activity,2)
        recycleViewActivities!!.layoutManager = popularLayoutManager
        recycleViewActivities!!.itemAnimator = DefaultItemAnimator()
        recycleViewActivities!!.adapter = popularAdapter

        val destination = ArrayList<Place>()
        prepareDestination(destination)
        val destinationAdapter = ExplorePlaceContentAdapter(this,destination, object: ExplorePlaceContentAdapter.OnItemClickListener{
            override fun onItemClick(position:Int){
                startActivity(Intent(context,TourDetailActivity::class.java))
            }
        })
        val destinationLayoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,true)
        recycleViewDestination!!.layoutManager = destinationLayoutManager
        recycleViewDestination!!.itemAnimator = DefaultItemAnimator()
        recycleViewDestination!!.adapter = destinationAdapter
    }

    private fun prepareDestination(destination: ArrayList<Place>) {
        destination.add(Place(R.drawable.tour1,"Tokyo"))
        destination.add(Place(R.drawable.tour1,"Singapore"))
        destination.add(Place(R.drawable.tour1,"Tokyo"))
        destination.add(Place(R.drawable.tour1,"Singapore"))
    }

    private fun prepareList(list: ArrayList<Trend>) {
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR198"))
        list.add(Trend(R.drawable.happycutomer,"Crime Histroy","MYR98"))
    }

}// Required empty public constructor

class Trend(val img : Int, val text : String, val price : String)

class Place(val pimg : Int, val pname : String)
