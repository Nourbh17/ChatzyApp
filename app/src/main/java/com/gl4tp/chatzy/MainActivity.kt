package com.gl4tp.chatzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.animation.content.Content
import com.gl4tp.chatzy.utils.NetworkConnectivityObserver
import com.gl4tp.chatzy.utils.NetworkStatus
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val networkConnectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)}
   // private lateinit var navController
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // here setKeepOnScreenCondition false so, activity redirect another activity
        // and some api call here
        // if setKeepOnScreenCondition true so, activity code not redirect another activity
        splashScreen.setKeepOnScreenCondition { false }
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),

           "No Internet Connection",
           Snackbar.LENGTH_INDEFINITE
        ).setAction("Wifi") {
           startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
       }
       networkConnectivityObserver.observe(this) {
           when (it) {
               NetworkStatus.Available -> {

                   if (snackbar.isShown) {
                       snackbar.dismiss()
                   }
               }
               else -> {

                   snackbar.show()
               }
           }
       }

       // val navHostFragment =
         //   supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        //navController
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, SecondActivity::class.java))
//            finish()
//        }, 2000)
    }



}