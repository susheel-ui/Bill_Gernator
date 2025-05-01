package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["ordId", "BarcodeId"],
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["ordId"], childColumns = ["ordId"]),
        ForeignKey(entity = items::class, parentColumns = ["BarcodeId"], childColumns = ["BarcodeId"])
    ]
)
data class OrderItem(
    val ordId: String,
    val BarcodeId: String,
    val quantity: Int,
    val total: Double
)
