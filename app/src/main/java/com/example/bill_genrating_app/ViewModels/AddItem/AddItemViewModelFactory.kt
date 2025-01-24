package com.example.bill_genrating_app.ViewModels.AddItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo

class AddItemViewModelFactory(private val repo: item_Repo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddItemViewModel(repo) as T
    }
}