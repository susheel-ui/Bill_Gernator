package com.example.bill_genrating_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.databinding.ActivityAddItemBinding

class AddItem : AppCompatActivity() {
    lateinit var thisActivityBinding:ActivityAddItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        thisActivityBinding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(thisActivityBinding.root)

    }
}