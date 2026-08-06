package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.response.Shop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ShopService {
    @GET("/api/shop/getShop/User")
    suspend fun getShopByUser(@Header("Authorization") token: String): Response<Shop>
}