package com.example.bill_genrating_app.Api.response

data class Order(
    val Order_items: List<OrderItem>,
    val clientName: String,
    val createdAt: String,
    val finalPrice: Double,
    val id: Int,
    val paymentMode: String,
    val paymentStatus: String,
    val totalMoney: Double
)