package com.example.bill_genrating_app.viewModels

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.repository.PreferencesRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.response.Metrices
import com.example.bill_genrating_app.Api.service.InventoryService
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.Api.service.PreferencesServices
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val inventoryService =
        ApiConfig.retrofit.create(InventoryService::class.java)
    private val orderServices = ApiConfig.retrofit.create(OrderServices::class.java)

    private val inventoryRepo = InventoryRepo(inventoryService)
    private val orderRepo = OrderRepo(orderServices)

    private val preferencesRepo  = PreferencesRepo(ApiConfig.retrofit.create(PreferencesServices::class.java))
    private val _recentTransaction = MutableLiveData<OrdersState>()
    val recentTransactionLiveData: LiveData<OrdersState> = _recentTransaction
    val sharedPreferences = SharePreferences(application)

    private val _allInventory = MutableLiveData<InventoryState>()
    val allInventoryLiveData: LiveData<InventoryState> = _allInventory

    private val _metrices = MutableLiveData<MetriceState>()
    val metricesLiveData: LiveData<MetriceState> = _metrices

    val token:String? = sharedPreferences.getToken()

    private val _DataLoading = MutableLiveData<Boolean>()
    val DataLoading: LiveData<Boolean> = _DataLoading



    init {
        recentTransaction()
        getAllInventory()
        getMatrices()
    }

    private fun getAllInventory() {
        viewModelScope.launch {
            _allInventory.postValue(InventoryState.Loading)
            val response = inventoryRepo.getInventoryAllItems(token!!)
            if(response.isSuccessful){
                if (response.code()== 200){
                    _allInventory.postValue(InventoryState.Success(response.body()!!))
                }else if(response.code() == 204){
                    _allInventory.postValue(InventoryState.Success(emptyList()))
                }
            }else{
                _allInventory.postValue(InventoryState.Failed(response.message(),response.code()))
            }
        }
    }

    fun refreshUi(){
        _DataLoading.value = true
            getAllInventory()
            recentTransaction()
            getMatrices()
        _DataLoading.value = false;
    }

    fun recentTransaction() {
        Log.d(TAG, "recentTransaction: loading")
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
    fun getMatrices() {
        viewModelScope.launch {
            _metrices.postValue(MetriceState.Loading)
            try {
                val response = preferencesRepo.getMetrices(token!!)
                if(response.isSuccessful){
                    _metrices.postValue(MetriceState.Success(response.body()!!))
                }else{
                    _metrices.postValue(MetriceState.Failed(response.message(),response.code()))
                }
            } catch (e: Exception) {
                Log.d(TAG, "getMatrices: exception")
            }
        }
    }
}

sealed class InventoryState{
    object Loading : InventoryState()
    data class Success(val data: List<Inventory>) : InventoryState()
    data class Failed(val message: String, val errorCode: Int) : InventoryState()
}
sealed class MetriceState{
    object Loading : MetriceState()
    data class Success(val data: Metrices) : MetriceState()
    data class Failed(val message: String, val errorCode: Int) : MetriceState()
}
