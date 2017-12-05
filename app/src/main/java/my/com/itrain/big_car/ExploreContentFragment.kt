package my.com.itrain.big_car


import `in`.goodiebag.carouselpicker.CarouselPicker
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_explore_content.*
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ExploreContentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_explore_content, container, false)

        return rootView
    }

}// Required empty public constructor
