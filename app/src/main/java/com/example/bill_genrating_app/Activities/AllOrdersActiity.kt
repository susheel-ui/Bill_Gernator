package com.example.bill_genrating_app.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityAllOrdersActiityBinding

class AllOrdersActiity : AppCompatActivity() {
    lateinit var  binding :ActivityAllOrdersActiityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityAllOrdersActiityBinding.inflate(layoutInflater)
       setContentView(binding.root)

        // TODO: create list to show all Orders

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay_static, R.anim.zoom_out)
    }
}