package com.example.bill_genrating_app.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.service.InventoryService
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import kotlinx.coroutines.launch
import java.util.Arrays

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val inventoryService =
        ApiConfig.retrofit.create(InventoryService::class.java)
    private val orderServices = ApiConfig.retrofit.create(OrderServices::class.java)

    private val inventoryRepo = InventoryRepo(inventoryService)
    private val orderRepo = OrderRepo(orderServices)
    private val _recentTransaction = MutableLiveData<OrdersState>()
    val recentTransactionLiveData: LiveData<OrdersState> = _recentTransaction
    val sharedPreferences = SharePreferences(application)

    private val _allInventory = MutableLiveData<InventoryState>()
    val allInventoryLiveData: LiveData<InventoryState> = _allInventory

    val token:String? = sharedPreferences.getToken()


    init {
        recentTransaction()
        getAllInventory()
        Log.d("Token", "token : $token ")
    }

    private fun getAllInventory() {
        viewModelScope.launch {
            _allInventory.postValue(InventoryState.Loading)
            val response = inventoryRepo.getInventoryItems(token!!)
            if(response.isSuccessful){
                _allInventory.postValue(InventoryState.Success(response.body()!!))
            }else{
                _allInventory.postValue(InventoryState.Failed(response.message(),response.code()))
            }
        }
    }

    fun recentTransaction() {

        _recentTransaction.value = OrdersState.Loading

        viewModelScope.launch {

            try {
                if (token.isNullOrBlank()) {
                    _recentTransaction.postValue(
                        OrdersState.Failed(
                            "Token not found",
                            401
                        )
                    )
                    return@launch
                }

//                val response = inventoryRepo.getInventoryItems(token)
                    val response = orderRepo.getAllOrders(token)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.size?.let {
                        if(it > 3){
                            val orders = response.body()!!.takeLast(3).reversed()
                            _recentTransaction.postValue(
                                OrdersState.Success(orders)
                            )
                        }
                        else
                            _recentTransaction.postValue(
                                OrdersState.Success(response.body())
                            )
                    }
                } else {
                    _recentTransaction.postValue(
                        OrdersState.Failed(
                            response.message(),
                            response.code()
                        )
                    )
                }

            } catch (e: Exception) {
                _recentTransaction.postValue(
                    OrdersState.Failed(
                        e.localizedMessage ?: "Unknown Error",
                        -1
                    )
                )
            }
        }
    }
}

sealed class InventoryState {
    object Loading : InventoryState()
    data class Success(val data: List<Inventory>) : InventoryState()
    data class Failed(val message: String, val errorCode: Int) : InventoryState()
}