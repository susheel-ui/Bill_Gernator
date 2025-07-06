package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shop_details",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )])
data class shopDetails(
    @PrimaryKey val id:Int, // This will be the foreign key
    val GSTIN:String,
    val shopName:String,
    val address:String,
    val contactNumber:String,
    val businessHours:String
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val username: String,
    val password: String  // In production, never store plain passwords.
)