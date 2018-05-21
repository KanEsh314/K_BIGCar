package my.com.itrain.big_car

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_type.*

class TypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val list = ArrayList<Type>()
        prepareList(list)
        val typeAdapter = TypeAdapter(applicationContext, list, object : TypeAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent()
                intent.putExtra("selectedType", position)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        val typeLayoutManger = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, true)
        recycleViewType!!.layoutManager = typeLayoutManger
        recycleViewType!!.itemAnimator = DefaultItemAnimator()
        recycleViewType!!.adapter = typeAdapter
    }

    private fun prepareList(list: ArrayList<Type>) {
        list.add(Type(R.mipmap.ic_action_personal, "Personal"))
        list.add(Type(R.mipmap.ic_action_personal, "Business"))
    }


}

class Type(val typeImg : Int, val typeText : String)

class TypeAdapter(private val context: Context, private val list: List<Type>, private val listener: TypeAdapter.OnItemClickListener): RecyclerView.Adapter<TypeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeImage: ImageView
        var typeTitle: TextView

        init {
            typeImage = itemView.findViewById(R.id.typeImage)
            typeTitle = itemView.findViewById(R.id.typeTitle)
        }

        fun bind(position: Int, listener: TypeAdapter.OnItemClickListener){
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onItemClick(position)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.multiple_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val trend : Type = list.get(position)
        holder?.typeImage?.setImageResource(trend.typeImg)
        holder?.typeTitle?.text = trend.typeText

        holder?.bind(position,listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}
