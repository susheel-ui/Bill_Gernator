package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    @PrimaryKey val ordId: String,
    val name: String,
    val mob: String,
    val grandTotal: Double,
    val status: String
)
