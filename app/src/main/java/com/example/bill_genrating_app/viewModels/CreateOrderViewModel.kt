package com.example.bill_genrating_app.viewModels

import android.app.Application
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.service.InventoryService
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.UtilClasses.UtilString
import com.example.bill_genrating_app.entity.invoiceItem
import kotlinx.coroutines.launch
import com.example.bill_genrating_app.UtilClasses.SharePreferences

class CreateOrderViewModel(application: Application): AndroidViewModel(application) {

    val clientName = "Guest"
    val mobileNumber = "12345678901"

    private val _itemList = MutableLiveData<ArrayList<Inventory>>()
    val itemList: LiveData<ArrayList<Inventory>> = _itemList
    val sharedPreferences = SharePreferences(application)
    val token = sharedPreferences.getToken()

    val invetoryRepo = InventoryRepo(ApiConfig.retrofit.create(InventoryService::class.java))

    fun addItem(barcodeId:String){
        viewModelScope.launch {
            Log.d(TAG, "addItem: token $token & barcode ->$barcodeId")
            val respoonse = invetoryRepo.getInventoryItemByBarcode(token!!,barcodeId)
            if(respoonse.isSuccessful){
                val item = respoonse.body()
                if(item != null){
                    addItemToInvoice(item)
                }
            }
        }
//        val list = _itemList.value ?: ArrayList()
//        list.add(item)
//        _itemList.value = list
    }
    fun addItemToInvoice(item: Inventory){
        val list = _itemList.value ?: ArrayList()
        list.add(item)
        _itemList.value = list
         }


}