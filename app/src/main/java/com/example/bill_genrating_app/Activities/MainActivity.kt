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

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding for current activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        change_fragment(invoice_fragment(), "invoices")
        setContentView(binding.root)


        // all listeners is here

        // fragement changing Actions
        this.binding.bottomNavBarlayout.itemOne.setOnClickListener {
            change_fragment(invoice_fragment(), "invoices")
        }
        this.binding.bottomNavBarlayout.itemTwo.setOnClickListener {
            change_fragment(items_fragment(), "Item")
        }
        this.binding.bottomNavBarlayout.itemthree.setOnClickListener {
            change_fragment(clients_fragments(), "clients")
        }
        this.binding.bottomNavBarlayout.itemFour.setOnClickListener {
            change_fragment(setting_fragment(), "setting")
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

    private fun change_fragment(Fg: Fragment, pagename: String) {
        var manager: FragmentManager = supportFragmentManager
        manager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
            )
            .replace(R.id.Container_view, Fg)
            .commit()

    }
}