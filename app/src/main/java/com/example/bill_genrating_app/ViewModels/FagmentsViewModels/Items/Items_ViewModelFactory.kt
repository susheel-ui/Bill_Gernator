package com.example.bill_genrating_app.ViewModels.FagmentsViewModels.Items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.DB_Repo

class Items_ViewModelFactory(private val repo:DB_Repo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Items_viewModel(repo) as T
    }
}