package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityMainBinding
import com.example.bill_genrating_app.Fragments.*
import com.example.bill_genrating_app.UtilClasses.change_fragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager = supportFragmentManager
        // binding for current activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        change_fragment(invoice_fragment(),binding.ContainerView.id, "invoices",fragmentManager)
        setContentView(binding.root)

        // all listeners is here

        // fragement changing Actions
        this.binding.bottomNavBarlayout.itemOne.setOnClickListener {
            change_fragment(invoice_fragment(),binding.ContainerView.id, "invoices",fragmentManager)
        }
        this.binding.bottomNavBarlayout.itemTwo.setOnClickListener {
            change_fragment(items_fragment(),binding.ContainerView.id, "Item",fragmentManager)
        }
        this.binding.bottomNavBarlayout.itemthree.setOnClickListener {
            change_fragment(clients_fragments(),binding.ContainerView.id, "clients",fragmentManager)
        }
        this.binding.bottomNavBarlayout.itemFour.setOnClickListener {
            change_fragment(setting_fragment(),binding.ContainerView.id, "setting",fragmentManager)
        }// Note: here itemOne,itemTwo,itemThree,itemFour is basically menu items because its a custom bottom navbar

//         add order button listner
        this.binding.bottomNavBarlayout.NewOrderbtn.setOnClickListener {
            val intent = Intent(applicationContext, OrderActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        super.onStart()
        // here we do code for the
    }


}