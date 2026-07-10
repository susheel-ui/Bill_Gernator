package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.AuthRepo
import com.example.bill_genrating_app.Api.service.AuthService
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import com.example.bill_genrating_app.databinding.ActivitySplashScreenBinding
import com.journeyapps.barcodescanner.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    lateinit var activity: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activity = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activity.root)
        ViewCompat.setOnApplyWindowInsetsListener(activity.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharePreferences = SharePreferences(application)
        val token = sharePreferences.getToken()

        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "onCreate: Splash screen Token ->$token ")
            delay(5000)
            val authRepo = AuthRepo(ApiConfig.retrofit.create(AuthService::class.java))
            val response = authRepo.validateUser(token!!)
                when(response.code()){
                    200 -> {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    }
                    403 -> {
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    }
                }
            finish()
        }
    }
}