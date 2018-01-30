package my.com.itrain.big_car


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_browse_content.*


/**
 * A simple [Fragment] subclass.
 */
class BrowseContentFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_browse_content, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = ArrayList<Categories>()
        prepareCategory(category)
        val categoryAdapter = CategoryAdapter(this, category)
        listViewCategory!!.adapter = categoryAdapter

        listViewCategory.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                startActivity(Intent(context, CategoryActivity::class.java))
            }

        })
    }

    private fun prepareCategory(category: ArrayList<Categories>) {
        category.add(Categories(R.drawable.tour1,"Sight Seeing","(14)"))
        category.add(Categories(R.drawable.tour2,"Museum Tour","(20)"))
        category.add(Categories(R.drawable.tour1,"Historical Building","(16)"))
        category.add(Categories(R.drawable.tour2,"Walking Tours","(11)"))
        category.add(Categories(R.drawable.tour1,"Eat & Drink","(20)"))
        category.add(Categories(R.drawable.tour2,"Churces","(8)"))
        category.add(Categories(R.drawable.tour1,"Skyline Tour","(11)"))
    }
}// Required empty public constructor

class Categories(val catImg : Int, val catTitle : String, val catTotal: String)
