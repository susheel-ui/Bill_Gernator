package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class items(
    @PrimaryKey
    val BarcodeItemId:Long,
    @ColumnInfo
    val itemName:String,
    @ColumnInfo
    val ItemQuantity:Int,
    @ColumnInfo
    val quantityType:String
) {

}