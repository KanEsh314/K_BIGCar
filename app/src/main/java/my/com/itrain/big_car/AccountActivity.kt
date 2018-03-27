package my.com.itrain.big_car

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_tour_count.*
import my.com.itrain.big_car.R.drawable.user

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setSupportActionBar(account_toolbar)

        val userMaterial = intent.getStringExtra("userMaterial")
        name.text = Editable.Factory.getInstance().newEditable("Test Name")
        hp_nbr.text = Editable.Factory.getInstance().newEditable("0000000000")
        address.text = Editable.Factory.getInstance().newEditable("Kuala Lumpur City Centre, 50088 Kuala Lumpur, Federal Territory of Kuala Lumpur")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {
                Toast.makeText(applicationContext, "Successfully Updated", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.account_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
