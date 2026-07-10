package com.example.bill_genrating_app.UtilClasses

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.repository.AuthRepo
import com.example.bill_genrating_app.Api.service.AuthService

class SharePreferences(application: Application) {
    private val sharedPreferences = application.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun logout(flag: Boolean): Boolean {
        sharedPreferences.edit {
            putString(UtilString.Token.toString(), null)
            apply()
        }
        return flag
    }

    fun getToken(): String? {
        return sharedPreferences.getString(UtilString.Token.toString(), "")
    }


    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}