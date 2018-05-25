package my.com.itrain.big_car

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog


class SplashActivity : Activity() {

    private val splash_screen = 2000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },splash_screen.toLong())
    }
}
