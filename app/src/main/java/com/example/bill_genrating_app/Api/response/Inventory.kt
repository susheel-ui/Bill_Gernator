package com.example.bill_genrating_app.Api.response

data class Inventory(
    val barcodeId: String,
    val categories: String,
    val createdAt: String,
    val description: String,
    val discountRate: Double,
    val finalPrice: Double,
    val id: Int,
    val name: String,
    val price: Double,
    val status: String,
    val stockQuantity: Int,
    val unitType: String,
    val updatedAt: String
)