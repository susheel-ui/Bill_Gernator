package com.example.bill_genrating_app.Api.response

data class OrderItem(
    val id: Int,
    val inventoryId: Int,
    val itemName:String,
    val quantity: Int,
    val discountRate: Double,
    val totalPrice: Double,
    val unitPrice: Double
)