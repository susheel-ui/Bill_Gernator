package com.example.bill_genrating_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.Activities.OrderActivity
import com.example.bill_genrating_app.databinding.ActivityPaymentOptionsBinding

class Payment_Options_Activity : AppCompatActivity() {
    lateinit var activityBinding: ActivityPaymentOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityPaymentOptionsBinding.inflate(layoutInflater)

        setContentView(activityBinding.root)

        activityBinding.layoutOptiontwo.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

    }
}