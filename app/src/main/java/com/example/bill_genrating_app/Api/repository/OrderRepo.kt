package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.payloads.OrderPayload
import com.example.bill_genrating_app.Api.response.CreateOrderResponse
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.Api.service.OrderServices
import retrofit2.Response

class OrderRepo(private val orderServices: OrderServices) {
    suspend fun createOrder(token: String, order: OrderPayload) : Response<CreateOrderResponse> {
        return orderServices.createOrder(token = "Bearer $token", order = order)
    }
    suspend fun getAllOrders(token: String) : Response<List<Order>> {
       return orderServices.getAllOrder("Bearer $token")
    }
    suspend fun getOrderById(token: String, id: String) : Response<Order> {
        return orderServices.getOrderById("Bearer $token", id)
    }
    suspend fun updateOrderPaymentStatus(token: String, id: String, status: String) : Response<Order> {
        return orderServices.updateOrderPaymentStatus("Bearer $token", id, status)
    }
}
