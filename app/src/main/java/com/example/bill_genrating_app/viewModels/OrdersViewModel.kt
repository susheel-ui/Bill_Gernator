package com.example.bill_genrating_app.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.UtilClasses.UtilString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersViewModel(application: Application) : AndroidViewModel(application) {
    private val orderServices = ApiConfig.retrofit.create(OrderServices::class.java)
    private val orderRepo = OrderRepo(orderServices)

    private val allOrders = MutableLiveData<OrdersState>()
    val allOrderLiveData: LiveData<OrdersState> = allOrders
    private val recentOrders = MutableLiveData<OrdersState>()
    val recentOrderLiveData: LiveData<OrdersState> = recentOrders

    private var token: String? = null

    init {
        token = getUserToken()
        refreshOrders()
    }

    fun getUserToken(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(UtilString.Token.toString(), null)
    }

    fun refreshOrders() {
        token?.let {
            getAllOrders(it)
        } ?: run {
            token = getUserToken()
            token?.let { getAllOrders(it) }
        }
    }

    fun getAllOrders(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            allOrders.postValue(OrdersState.Loading)
            recentOrders.postValue(OrdersState.Loading)
            val result = orderRepo.getAllOrders(token)
            if (result.isSuccessful) {
                allOrders.postValue(OrdersState.Success(result.body()))
                recentOrders.postValue(OrdersState.Success(result.body()))
            } else {
                allOrders.postValue(OrdersState.Failed(result.message(), result.code()))
                recentOrders.postValue(OrdersState.Failed(result.message(), result.code()))
            }
        }
    }
}

sealed class OrdersState {
    object Loading : OrdersState()
    data class Success(val data: List<Order>?) : OrdersState()
    data class Failed(val message: String, val code: Int) : OrdersState()
}
