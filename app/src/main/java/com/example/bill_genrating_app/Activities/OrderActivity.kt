package com.example.bill_genrating_app.Activities

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    var activity:ActivityOrderBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        activity = ActivityOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activity!!.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()
    }

}