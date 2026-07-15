package com.example.bill_genrating_app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.payloads.InventoryPayload
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.service.InventoryService
import kotlinx.coroutines.launch
import com.example.bill_genrating_app.UtilClasses.SharePreferences

class CreateInventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _createInventory= MutableLiveData<CreateInventoryState>()
    val createInventory: LiveData<CreateInventoryState> = _createInventory

    private val repo = InventoryRepo(ApiConfig.retrofit.create(InventoryService::class.java))
    private val token = SharePreferences(application).getToken()
    fun saveInventory(inventory: InventoryPayload) {
        _createInventory.postValue(CreateInventoryState.Loading)
        viewModelScope.launch {
            token?.let {
                val response = repo.createInventoryItem(token,inventory)
                if (response.isSuccessful){
                    _createInventory.postValue(CreateInventoryState.Success(response.body()!!))
                }else{
                    _createInventory.postValue(CreateInventoryState.Failed(response.code(),response.message()))
                }
            }
        }
    }
}
sealed class CreateInventoryState{
    object Loading : CreateInventoryState()
    class Success(data: Inventory) : CreateInventoryState()
    class Failed(ErrorCode:Int,ErrorMessage:String) : CreateInventoryState()
}