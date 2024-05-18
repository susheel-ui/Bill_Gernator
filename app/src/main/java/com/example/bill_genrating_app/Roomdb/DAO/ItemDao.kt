package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bill_genrating_app.Roomdb.entities.items

@Dao
interface itemDao {
        @Query("Select * from items")
        fun getall():List<items>

        @Query("SELECT * FROM ITEMS WHERE BarcodeId=(:barcodeId)")
        fun getByid(barcodeId:Long):List<items>

        @Insert
        fun SaveNewItem(vararg items: items)

        @Delete
        fun DeleteItem(items: items)
}