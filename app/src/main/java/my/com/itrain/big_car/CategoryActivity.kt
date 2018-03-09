package my.com.itrain.big_car

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_category.*
import java.util.ArrayList

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setSupportActionBar(toolbarActivity)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val trending = ArrayList<Popular>()
        preparePopular(trending)
        val trendingAdapter = ExplorePopularContent(this, trending, object: ExplorePopularContent.OnItemClickListener{
            override fun onItemClick(position: Int) {
                Log.d("Next Page","Will Have Soon")
            }
        })
        val trendingLayoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, true)
        recycleViewCategory!!.layoutManager = trendingLayoutManager
        recycleViewCategory!!.itemAnimator = DefaultItemAnimator()
        recycleViewCategory!!.adapter = trendingAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun preparePopular(trending: ArrayList<Popular>) {
        trending.add(Popular(R.drawable.tour1, "HALF DAY KUALA LUMPUR CITY TOUR TOUR","For short or long tern visitors in Kuala Lumpur this is perfect opportunity to visit landmarks to Kuala Lumpur city. On tour we will take you to some of most important sightseeing attractions.",0F, "15","RM45"))
        trending.add(Popular(R.drawable.tour1, "KL CITY TOUR AND KL TOWER VISIT","A must of first time visitors to Kuala Lumpur. An interesting tour which unveils the beauty and charm of the old and new Kuala Lumpur – Garden City of Lights. See the contract of he magnificent Skyscrapers and the buildings of the colonial day.",1F, "75","RM45"))
        trending.add(Popular(R.drawable.tour1, "KL TOWER NIGHT TOUR","Lorem ipsum dolor sit amet, quem convenire interesset ut vix, ad dicat sanctus detracto vis. Eos modus dolorum ex, qui adipisci maiestatis inciderint no, eos in elit dicat.....",0F, "35","RM45"))
        trending.add(Popular(R.drawable.tour1, "HALF DAY TEMPLE AND KL TOWER","It is also a perfect place to relax and enjoy the panoramic view of the temple set against the Kuala Lumpur skyline.",1F, "95","RM45"))
    }

}

class Popular(val popularimg : Int, val popularname : String, val populardesc : String, val popularrating : Float, val popularratingtext : String, val popularprice : String)
