package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bill_genrating_app.Roomdb.entities.items

@Dao
interface itemDAO {
    @Query("SELECT * FROM items")
    fun GetAll():List<items>

    @Query("SELECT * FROM items where BarcodeItemId= (:barcode)")
    fun getFromBarcode(barcode:Int)

    @Insert
    fun insertNewData(vararg items:items)

    @Delete
    fun remove(delete: Delete)
}