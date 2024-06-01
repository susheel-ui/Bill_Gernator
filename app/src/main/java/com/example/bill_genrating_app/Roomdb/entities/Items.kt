package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class items(
    @PrimaryKey
    val BarcodeId:Long,
    @ColumnInfo
    val Name:String,
    @ColumnInfo
    val weight:String,
    @ColumnInfo
    val weightType:String,
    @ColumnInfo
    val Type:String,
    @ColumnInfo
    val MRP:Double
)