package my.com.itrain.big_car

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by iTrain on 18-Dec-17.
 */

public class Adapter(private val context: Context, private val inflater: LayoutInflater, private val images: ArrayList<Int>):PagerAdapter(){

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val myImageLayout = inflater.inflate(R.layout.pic_slide, container,false )
        val myImage = myImageLayout.findViewById<ImageView>(R.id.pic_image)
        myImage.setImageResource(images.get(position))
        container!!.addView(myImageLayout,0)
        return myImageLayout
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(View(`object` as Context?))
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view!!.equals(`object`)
    }

    override fun getCount(): Int {
        return images.size
    }

}
