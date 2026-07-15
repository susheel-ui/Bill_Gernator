package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.payloads.OrderPayload
import com.example.bill_genrating_app.Api.response.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderServices {
    @POST("/api/orders/create")
    suspend fun createOrder(@Header("Authorization") token: String, @Body order: OrderPayload): Response<Order>
    @GET("api/orders/all")
    suspend fun getAllOrder(@Header("Authorization") token: String): Response<List<Order>>

    @GET("api/orders/get/{id}")
    suspend fun getOrderById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Order>

    @PUT("api/orders/update-status/{id}")
    suspend fun updateOrderPaymentStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("status") status: String
    ): Response<Order>

}