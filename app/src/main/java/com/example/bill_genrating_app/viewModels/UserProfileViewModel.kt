package com.example.bill_genrating_app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.AuthRepo
import com.example.bill_genrating_app.Api.repository.ShopRepo
import com.example.bill_genrating_app.Api.response.Shop
import com.example.bill_genrating_app.Api.response.User
import com.example.bill_genrating_app.Api.service.AuthService
import com.example.bill_genrating_app.Api.service.ShopService
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import kotlinx.coroutines.launch
import kotlin.jvm.java

class UserProfileViewModel(application: Application) : AndroidViewModel(application){

    private val _shopDetails  = MutableLiveData<ShopState>()
    val shopDetails: LiveData<ShopState> = _shopDetails

    private val _user = MutableLiveData<UserState>()
    val user: LiveData<UserState> = _user

    val token  = SharePreferences(application).getToken()
    val shopRepo = ShopRepo(ApiConfig.retrofit.create(ShopService::class.java))
    val authRepo = AuthRepo(ApiConfig.retrofit.create(AuthService::class.java))
    init {
        getShopDetails()
        getUserDetials()
    }

    private fun getUserDetials() {
      viewModelScope.launch {
          token?.let {
             val resoponse =  authRepo.getUserDetails(it)
              if(resoponse.isSuccessful){
                  resoponse.body()?.let {
                      _user.value = UserState.Success(it)
                  }
              }else{
                  _user.value = UserState.Failes(resoponse.message(), resoponse.code())
              }
          }
      }
    }

    fun getShopDetails(){
        _shopDetails.value = ShopState.Loading
       viewModelScope.launch {
           token?.let {token ->
               shopRepo.getShopByUser(token).let {
                   if(it.isSuccessful){
                       _shopDetails.value = ShopState.Success(it.body()!!)
                   }else{
                       _shopDetails.value = ShopState.Failes(it.message(), it.code())
                   }
               }
           }
       }
    }
}
sealed class ShopState{
    object Loading: ShopState()
    class Success(val data: Shop): ShopState()
    class Failes(val message: String, val code: Int): ShopState()
}
//TODO::FUTURE FOR USER DETAILS
sealed class UserState{
    object Loading: UserState()
    class Success(val data: User): UserState()
    class Failes(val message: String, val code: Int): UserState()
}
