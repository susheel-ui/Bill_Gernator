package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.Api.response.Metrices
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface PreferencesServices {



    @GET("/api/dashboard/metrics")
    suspend fun getMetrices(@Header("Authorization") token: String): Response<Metrices>
}