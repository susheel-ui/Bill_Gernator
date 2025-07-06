package com.example.bill_genrating_app.Activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.databinding.ActivityMainBinding
import com.example.bill_genrating_app.Fragments.*
import com.example.bill_genrating_app.Roomdb.Repos.UserService
import com.example.bill_genrating_app.UtilClasses.change_fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var invoiceFragment:invoice_fragment
    lateinit var clientsFragments: clients_fragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager = supportFragmentManager
        // binding for current activity
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        var id: Int? = null
        try {
            id = intent.extras?.getInt("_id")
            Log.d(TAG, "onCreate: Main Activity id =  $id")
        } catch (e: Exception) {
            Log.d("Error MainActivity : getIntentError", "onCreate: ${e.message}")
        }
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                id?.let { UserService(applicationContext).getUserById(it.toLong()) }
            }
            Log.d(TAG, "onCreate: ${user?.username.toString()}")
            invoiceFragment = invoice_fragment(user)
            clientsFragments = clients_fragments(user)
            change_fragment(invoiceFragment,binding.ContainerView.id, "invoices",fragmentManager)
        }





        // all listeners is here

        // fragment changing Actions
        this.binding.bottomNavBarlayout.itemOne.setOnClickListener {
            change_fragment(invoiceFragment,binding.ContainerView.id, "invoices",fragmentManager)
        }
        this.binding.bottomNavBarlayout.itemTwo.setOnClickListener {
            change_fragment(items_fragment(),binding.ContainerView.id, "Item",fragmentManager)
        }
        this.binding.bottomNavBarlayout.itemthree.setOnClickListener {
            change_fragment(clientsFragments,binding.ContainerView.id, "clients",fragmentManager)
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
    }


}