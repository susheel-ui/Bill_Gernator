package com.example.bill_genrating_app.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding

class FinalOrderActivity : AppCompatActivity() {
    lateinit var  activityBinding: ActivityFinalOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)


    }
}