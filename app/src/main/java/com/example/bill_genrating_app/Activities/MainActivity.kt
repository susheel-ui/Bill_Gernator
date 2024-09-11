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

   lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding for current activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        change_fragment(invoice_fragment(),"invoices")
        setContentView(binding.root)

        // all listeners is here
        binding.btmnavbar.setOnItemSelectedListener {item->
            when(item.itemId){
                R.id.invoice ->{
                    change_fragment(invoice_fragment(),"invoices")
                    true
                }
                R.id.Client ->{
                    change_fragment(clients_fragments(),"Clients")
                    true
                }
                R.id.items ->{
                    change_fragment(items_fragment(),"items")
                    true

                }
                R.id.setting ->{
                    change_fragment(setting_fragment(),"Setting")
                    true
                }
                else->{
                    true
                }


            }
        }

        // add order button listner
            this.binding.NewOrderbtn.setOnClickListener {
                val intent = Intent(applicationContext,OrderActivity::class.java)
                startActivity(intent)
            }


    }

    override fun onStart() {
        super.onStart()
        // here we do code for the


    }
    private fun change_fragment(Fg:Fragment,pagename:String){
        var manager:FragmentManager = supportFragmentManager
       manager.beginTransaction().replace(R.id.Container_view,Fg).commit()
        binding.inoviceTextPage.text = pagename.uppercase()

    }
}