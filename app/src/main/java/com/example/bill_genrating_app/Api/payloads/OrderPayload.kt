package com.example.bill_genrating_app.Api.payloads

data class OrderPayload(
    val customerName: String,
    val orderItems: List<OrderItem>,
    val paymentMethod: String,
    val paymentStatus: String
)