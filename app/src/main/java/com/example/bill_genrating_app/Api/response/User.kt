package com.example.bill_genrating_app.Api.response

data class User(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Authority>,
    val credentialsNonExpired: Boolean,
    val email: String,
    val enabled: Boolean,
    val id: Int,
    val mobileNo: String,
    val password: String,
    val role: String,
    val shop: Shop,
    val username: String
)