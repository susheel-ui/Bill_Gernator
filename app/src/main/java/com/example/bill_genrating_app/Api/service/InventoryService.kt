package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.response.Inventory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface InventoryService {
    @GET("/inventory/all")
    suspend fun getInventoryItems(@Header("Authorization") token: String): Response<List<Inventory>>

}
