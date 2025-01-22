package com.example.bill_genrating_app.ViewModels.AddItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Roomdb.DB_Repo
import com.example.bill_genrating_app.Roomdb.entities.items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemViewModel(private val repo:DB_Repo):ViewModel() {

    fun saveItem(item:items){
        viewModelScope.launch(Dispatchers.IO) {
            repo.SaveItem(item)
        }
    }

}