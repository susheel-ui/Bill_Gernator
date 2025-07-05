package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.Repos.shopDetailsService
import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import com.example.bill_genrating_app.databinding.ActivityShopDetailsEditPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class ShopDetailsEditPage : AppCompatActivity() {
    lateinit var binding: ActivityShopDetailsEditPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopDetailsEditPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.extras?.getLong("_id")
        Log.d(TAG, "onCreate: id is passes sucessfull $id")

        binding.btnSave.setOnClickListener {
            setDatatodb(id);
        }
        lifecycleScope.launch {
            val job = CoroutineScope(Dispatchers.IO).async {
                id?.let { shopDetailsService(applicationContext).getShopDetails(it.toInt())}
            }.await()
            CoroutineScope(Dispatchers.Main).launch {
                binding.ShopName.setText(job?.shopName)
                binding.GSTIN.setText(job?.GSTIN)
                binding.Address.setText(job?.address)
                binding.Mobile.setText(job?.contactNumber)
                binding.BusinessHours.setText(job?.businessHours)
            }
        }
    }
    fun setDatatodb(id:Long?){
        if (
            binding.ShopName.text.toString().isNotEmpty() ||
            binding.GSTIN.text.toString().isNotEmpty() ||
            binding.Address.text.toString().isNotEmpty() ||
            binding.Mobile.text.toString().isNotEmpty() ||
            binding.BusinessHours.text.toString().isNotEmpty()
        ) {
            val shopDetails = getDataFromFields(id!!.toInt())
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    binding.btnSave.isEnabled = false
                    binding.btnSave.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                }
                try {
                    shopDetailsService(applicationContext).insertShopDetails(shopDetails)
                } catch (e: Exception) {
                    Log.d("Error", "setDatatodb: ${e.message}")
                    try {
                        shopDetailsService(applicationContext).updateShopDetails(shopDetails)
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "DB Connectivity Problem", Toast.LENGTH_SHORT).show()
                    }
                }
                delay(1000)
            }.invokeOnCompletion {
                  lifecycleScope.launch {
                      withContext(Dispatchers.Main) {
                          binding.btnSave.isEnabled = true
                          binding.btnSave.setBackgroundColor(resources.getColor(R.color.colorPrimaryVariant))
                          setResult(RESULT_OK)
                          finish()
                      }
                  }
            }
        } else {
            Toast.makeText(this, "Fields is empty", Toast.LENGTH_SHORT).show()
        }
    }
    fun getDataFromFields(id: Int): shopDetails{
        val shopName = binding.ShopName.text.toString()
        val gstin = binding.GSTIN.text.toString()
        val address = binding.Address.text.toString()
        val mobile = binding.Mobile.text.toString()
        val businessHours = binding.BusinessHours.text.toString()
        return shopDetails(id,gstin, shopName, address, mobile, businessHours)
    }
}