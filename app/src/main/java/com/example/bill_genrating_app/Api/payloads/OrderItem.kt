package com.example.bill_genrating_app.Api.payloads

data class OrderItem(
    val discountRate: Int,
    val inventoryId: Int,
    val quantity: Int,
    val unitPrice: Int
)