package com.example.bill_genrating_app.ViewModels.FagmentsViewModels.Items

import androidx.lifecycle.ViewModel
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo
import com.example.bill_genrating_app.Roomdb.entities.items

class Items_viewModel(private val repo: item_Repo):ViewModel() {
    fun getItemList() :List<items>{
        return repo.getItems()
    }
    fun getItemBYId(id:Long):List<items> {
        return repo.getById(id)
    }
    fun getItemByName(str:String):List<items> {
        return repo.getByName(str)
    }

}