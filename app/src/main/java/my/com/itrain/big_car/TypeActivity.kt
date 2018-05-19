package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        Log.d("Debug", intent.getStringExtra("typeMaterial"))
    }


}
