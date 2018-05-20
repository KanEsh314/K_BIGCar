package my.com.itrain.big_car

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_type.*
import my.com.itrain.big_car.R.id.listViewType

class TypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val typeMaterial = intent.getSerializableExtra("typeMaterial") as ArrayList<String>

        Toast.makeText(applicationContext, typeMaterial.toString(), Toast.LENGTH_LONG).show()


        val listView = TypeAdapter(applicationContext, typeMaterial)
        listViewType.adapter = listView
    }


}

class TypeAdapter(private val context: Context, private val typeMaterial:ArrayList<String>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.multiple_type, parent, false)
        val typeTitle = view.findViewById(R.id.typeTitle) as TextView
        typeTitle.text = typeMaterial.toString()
        return view
    }

    override fun getItem(position: Int): Any {
        return typeMaterial[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return typeMaterial.size
    }
}
