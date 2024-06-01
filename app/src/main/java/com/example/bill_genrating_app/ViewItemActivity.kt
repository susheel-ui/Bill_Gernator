package com.example.bill_genrating_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.databinding.ActivityViewItemBinding

class ViewItemActivity : AppCompatActivity() {
    lateinit var thisPageBinding:ActivityViewItemBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        thisPageBinding = ActivityViewItemBinding.inflate(layoutInflater)
        setContentView(thisPageBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onStart() {
        super.onStart()
        val barcodeid= intent.getStringExtra("itemId")
        //this is for only test purpose
        thisPageBinding.textview.text = barcodeid.toString()

    }
}