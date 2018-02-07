package my.com.itrain.big_car


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val auth = rootView.findViewById<View>(R.id.auth_button)
        auth.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(context, StartActivity::class.java))
            }
        })

        return rootView
    }

}// Required empty public constructor
