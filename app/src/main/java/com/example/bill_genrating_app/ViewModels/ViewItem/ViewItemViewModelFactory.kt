package com.example.bill_genrating_app.ViewModels.ViewItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo

class ViewItemViewModelFactory(private val repo: item_Repo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewItemViewModel(repo) as T
    }
}