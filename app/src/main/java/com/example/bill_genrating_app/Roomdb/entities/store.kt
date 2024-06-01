package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.Entity

@Entity(tableName = "store")
data class store(
    val barcodeid:Long,
    val quntityAvailble:Long
){

}