package com.example.bill_genrating_app.ViewModels.AddItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.DB_Repo

class AddItemViewModelFactory(private val repo:DB_Repo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddItemViewModel(repo) as T
    }
}