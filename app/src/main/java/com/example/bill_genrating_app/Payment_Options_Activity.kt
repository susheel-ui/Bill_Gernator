package com.example.bill_genrating_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import com.example.bill_genrating_app.Activities.OrderActivity
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import com.example.bill_genrating_app.databinding.ActivityPaymentOptionsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Payment_Options_Activity : AppCompatActivity() {
    lateinit var activityBinding: ActivityPaymentOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityPaymentOptionsBinding.inflate(layoutInflater)

        setContentView(activityBinding.root)

        val sharePreferences = SharePreferences(application)
        val token = sharePreferences.getToken()

        val orderRepo = OrderRepo(ApiConfig.retrofit.create(OrderServices::class.java))

        activityBinding.layoutOptiontwo.setOnClickListener {
            setResult(RESULT_OK)
            val id = intent.getStringExtra("OrderId")!!
            Log.d(TAG, "onPayemntPage: token -> $token \n id -> $id")
            CoroutineScope(Dispatchers.IO).launch{
                val response = orderRepo.updateOrderPaymentStatus(token!!, id, "PAID")
                if(response.isSuccessful){
                    finish()
                }
            }
        }
        activityBinding.backBtn.setOnClickListener {
            finish()
        }

    }

}