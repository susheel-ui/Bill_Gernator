package com.example.bill_genrating_app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.service.InventoryService
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import kotlinx.coroutines.launch

class ViewInventoryViewModel(application: Application) : AndroidViewModel(application){

    private val _inventory= MutableLiveData<InventoryState>()
    val inventory: LiveData<InventoryState> = _inventory

    val repo = InventoryRepo(ApiConfig.retrofit.create(InventoryService::class.java))
    val token = SharePreferences(application).getToken()

    fun setInventoryId(id:Int){
        _inventory.value = InventoryState.Loading
        viewModelScope.launch {
            token?.let {
                val response = repo.getInventoryById(token,id)
                if(response.isSuccessful){
                    _inventory.value = InventoryState.Success(response.body()!!)
                }else{
                    _inventory.value = InventoryState.Failed(response.code(),response.message())
                }
            }
        }
    }
    sealed class InventoryState(){
        object Loading: InventoryState()
        class Success(val data: Inventory): InventoryState()
        class Failed(val errorCode:Int,val message:String?): InventoryState()
    }
}
