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
import com.example.bill_genrating_app.Api.payloads.OrderItem
import com.example.bill_genrating_app.Api.payloads.OrderPayload
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.service.InventoryService
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.UtilClasses.UtilString
import com.example.bill_genrating_app.entity.invoiceItem
import kotlinx.coroutines.launch
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import com.example.bill_genrating_app.entity.Invoice_item
import kotlin.math.PI

class CreateOrderViewModel(application: Application): AndroidViewModel(application) {

    private val _clientName = MutableLiveData<String>()
    val clientName: LiveData<String> = _clientName


    private val _orderApiCalling = MutableLiveData<OrderSave>()
    val orderApiCalling: LiveData<OrderSave> = _orderApiCalling

    private val _grandTotal = MutableLiveData<Double>()
    val grandTotal: LiveData<Double> = _grandTotal


    private val _itemList = MutableLiveData<ArrayList<Invoice_item>>()
    val itemList: LiveData<ArrayList<Invoice_item>> = _itemList
    val sharedPreferences = SharePreferences(application)
    val token = sharedPreferences.getToken()

    val invetoryRepo = InventoryRepo(ApiConfig.retrofit.create(InventoryService::class.java))

    fun setName(name: String){
        _clientName.value = name
    }

    fun addItem(barcodeId:String){
        viewModelScope.launch {
            Log.d(TAG, "addItem: token $token & barcode ->$barcodeId")
            val respoonse = invetoryRepo.getInventoryItemByBarcode(token!!,barcodeId)
            if(respoonse.isSuccessful){
                val item = respoonse.body()
                if(item != null){
                    addItemToInvoice(
                        Invoice_item(
                            id = item.id,
                            barCodeId = item.id.toLong(),
                            name = item.name,
                            initialMRP = item.price,
                            initialQuantity = 1,
                            initialDiscount = item.discountRate,
                            total = item.finalPrice
                    )
                    )
                }
            }
        }
//        val list = _itemList.value ?: ArrayList()
//        list.add(item)
//        _itemList.value = list
    }
    fun addItemToInvoice(item: Invoice_item){
        val list = _itemList.value ?: ArrayList()
        var found = false
        list.forEach {
            if (it.barCodeId == item.barCodeId) {
                it.initialQuantity = it.initialQuantity + 1
                it.total = it.initialQuantity * it.initialMRP - (it.initialDiscount / 100) * it.initialMRP *it.initialQuantity
                found = true
            }
        }
        if(!found){
            list.add(item)
        }
        _itemList.value = list
        calculateGrandTotal()
    }

    fun removeItem(id: Int) {
        val list = _itemList.value ?: return
        list.removeAll { it.id == id }
        _itemList.value = list
        calculateGrandTotal()
    }

    fun updateQuantity(id: Int, isIncrement: Boolean) {
        val list = _itemList.value ?: return
        list.find { it.id == id }?.let {
            if (isIncrement) {
                it.initialQuantity++
            } else if (it.initialQuantity > 1) {
                it.initialQuantity--
            }
            it.total = (it.initialQuantity * it.initialMRP)-(it.initialDiscount/100)*it.initialMRP*it.initialQuantity
        }
        _itemList.value = list
        calculateGrandTotal()
    }

     fun calculateGrandTotal() {
        val grandTotal = _itemList.value?.sumOf { it.total } ?: 0.0
        Log.d(TAG, "calculateGrandTotal: $grandTotal")
        _grandTotal.postValue(grandTotal)
    }

    fun SaveOrder(){
        _orderApiCalling.value = OrderSave.Loading
        val orderitem = ArrayList<OrderItem>()
        for (x in itemList.value!!){
            orderitem.add(
                OrderItem(
                    inventoryId = x.id,
                    unitPrice = x.initialMRP.toInt(),
                    quantity = x.initialQuantity,
                    discountRate = x.initialDiscount.toInt()
                )
            )
        }
        val orderPayload = OrderPayload(
            customerName = _clientName.value.toString(),
            orderItems = orderitem,
            paymentMethod = "CASH",
            paymentStatus = "PENDING"
        )
        viewModelScope.launch {
            val orderServices = ApiConfig.retrofit.create(OrderServices::class.java)
            val orderRepo = OrderRepo(orderServices)
            val response =orderRepo.createOrder(token!!,orderPayload)
            if(response.isSuccessful){
                Log.d(TAG, "SaveOrder: ${response.body()}")
                _orderApiCalling.value = OrderSave.Success(response.code(),response.body()!!.orderId)
            }
            else{
                Log.d(TAG, "SaveOrder: ${response.errorBody()}")
                _orderApiCalling.value = OrderSave.Failed(response.message())
            }
        }
    }

}

sealed class OrderSave{
    object Loading: OrderSave()
    class Success(val code:Int,val orderId:Int): OrderSave()
    class Failed(val message: String): OrderSave()
}
