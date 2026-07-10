package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.payloads.LoginRequest
import com.example.bill_genrating_app.Api.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/user/check-auth")
    suspend fun validateUser(@Header("Authorization") token: String): Response<Void>

}