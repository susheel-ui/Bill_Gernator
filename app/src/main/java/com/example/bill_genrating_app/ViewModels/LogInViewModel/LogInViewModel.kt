package com.example.bill_genrating_app.ViewModels.LogInViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo
import com.example.bill_genrating_app.Roomdb.Repo.user_Repo
import com.example.bill_genrating_app.Roomdb.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ViewModel for Login Activity
class LogInViewModel(private val userRepo : user_Repo) : ViewModel() {

    fun Login(usrId: String, psw: String): Boolean {  // usr = user and psw = password
        val result = userRepo.LoginUsr(usrId, psw)
        if (result.isNotEmpty()) {
            return true
        }
        return false
    }
    fun Resister(usrId: String,psw: String){
        val user = User(usrId,psw)
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.resisterUser(user)
        }
    }
    fun UpdateUser(usrId: String,psw: String){
        val user = User(usrId,psw)
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.updateUser(user)
        }
    }
}