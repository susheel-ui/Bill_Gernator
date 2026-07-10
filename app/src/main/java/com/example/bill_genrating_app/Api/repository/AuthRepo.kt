package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.payloads.LoginRequest
import com.example.bill_genrating_app.Api.service.AuthService

class AuthRepo(private val authService: AuthService) {
    suspend fun loginUser(body: LoginRequest) = authService.login(body)
    suspend fun validateUser(token:String) = authService.validateUser("Bearer $token")
}