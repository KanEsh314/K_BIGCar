package my.com.itrain.big_car


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_profile_content.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileContentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_profile_content, container, false)

        setHasOptionsMenu(true);
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_main_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}// Required empty public constructor
