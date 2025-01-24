package com.example.bill_genrating_app.ViewModels.ViewItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo
import com.example.bill_genrating_app.Roomdb.entities.items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewItemViewModel(private val repo: item_Repo):ViewModel() {
    fun getItemList() :List<items>{
        return repo.getItems()
    }
    fun getItemBYId(id:Long):List<items> {
        return repo.getById(id)
    }
    fun getItemByName(str:String):List<items>{
        return repo.getByName(str)
    }
    fun deleteItem(item: items){
        viewModelScope.launch(Dispatchers.IO) {
            repo.DeleteItem(item)
        }
    }
    fun updateItemsQuantity(stockQuantity:Long,barcodeId:Long){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItemsQuantity(stockQuantity,barcodeId)
        }
    }
}