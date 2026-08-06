package com.example.bill_genrating_app.Api.payloads

data class InventoryPayload(
    val barcodeId: String,
    val categories: String,
    val description: String,
    val discountRate: Double,
    val name: String,
    val price: Double,
    val status: String,
    val stockQuantity: Int,
    val unitType: String
)