package com.example.bill_genrating_app.Roomdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey
    private val UserId:String,
    @ColumnInfo
    private val Password:String
) {
}