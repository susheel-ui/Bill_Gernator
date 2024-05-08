package com.example.bill_genrating_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bill_genrating_app.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

   lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding for current activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        change_fragment(invoice_fragment(),"invoices")
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // here we do code for the
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
                R.id.reports ->{
                    change_fragment(reports_fragment(),"Reports")
                    true
                }
                R.id.setting ->{
                    change_fragment(setting_fragment(),"Setting")
                    true
                }
                else->{
                    // here code never comes it add because the compiler gives error
                true
                }


            }
        }


    }
    private fun change_fragment(Fg:Fragment,pagename:String){
        var manager:FragmentManager = supportFragmentManager
       manager.beginTransaction().replace(R.id.Container_view,Fg).commit()
        binding.inoviceTextPage.text = pagename.uppercase()

    }
}