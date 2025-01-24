package com.example.bill_genrating_app.ViewModels.LogInViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.Repo.user_Repo

// Login page View Model Factory
class LogInViewModelFactory(private val repo: user_Repo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogInViewModel(repo) as T
    }
}