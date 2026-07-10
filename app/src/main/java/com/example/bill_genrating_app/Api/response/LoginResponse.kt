package com.example.bill_genrating_app.Api.response

data class LoginResponse(
    val email: String,
    val id: Int,
    val message: String,
    val token: String,
    val username: String
)