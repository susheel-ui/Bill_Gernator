package com.example.bill_genrating_app.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.payloads.LoginRequest
import com.example.bill_genrating_app.Api.response.LoginResponse
import com.example.bill_genrating_app.Api.service.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private  var  authService: AuthService

    private val loginState = MutableLiveData<LoginState>()
    val loginStateLiveData = loginState
    init {
        authService = ApiConfig.retrofit.create(AuthService::class.java)
    }
    fun login(email:String,password:String){
        CoroutineScope(Dispatchers.IO).launch {
            loginState.postValue(LoginState.Loading)
            try {
                val result = authService.login(LoginRequest(email,password))
                loginState.postValue(LoginState.Success(result.body(),result.code()))
            }catch (e: Exception){
                Log.d("Debug", "login: ${e.message}")
            }
        }
    }

}
sealed class LoginState{
    object Loading:LoginState()
    data class Success(val message: LoginResponse?, val code: Int):LoginState()
    data class Error(val message:String,val code:Int):LoginState()
}
