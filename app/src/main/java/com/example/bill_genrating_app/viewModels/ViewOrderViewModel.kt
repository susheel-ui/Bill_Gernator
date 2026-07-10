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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.jvm.java
import kotlin.reflect.KClass



class ViewOrderViewModel(application: Application,val id:String): AndroidViewModel(application) {

   val orderRepo: OrderRepo
   val sharePreferences = application.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
   val token:String?
   private val _order = MutableLiveData<OrderState>()
    val orderLiveData: LiveData<OrderState> = _order


        init {
            val orderServices = ApiConfig.retrofit.create(OrderServices::class.java)
            orderRepo = OrderRepo(orderServices)
            token = sharePreferences.getString(UtilString.Token.toString(), null)
            getOrderById()
        }
    fun getOrderById(){
        _order.value = OrderState.Loading
        viewModelScope.launch {
           token?.let {
               val result = orderRepo.getOrderById(it,id)
               if (result.isSuccessful){
                   _order.postValue(OrderState.Success(result.body()!!))
               }else{
                   _order.postValue(OrderState.Failed(result.message(),result.code()))
               }
           }
        }
    }
}
sealed class OrderState{
    object Loading: OrderState()
    data class Success(val data: Order): OrderState()
    data class Failed(val message: String, val code: Int): OrderState()
}
