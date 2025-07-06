package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bill_genrating_app.Roomdb.entities.shopDetails

@Dao
interface ShopDetailsDao {
    @Insert
    suspend fun insertShopDetails(shopDetails: shopDetails)

    @Update
    suspend fun updateShopDetails(shopDetails: shopDetails)

    @Query("SELECT * FROM shop_details WHERE id = :id")
    suspend fun getShopDetails(id: Int): shopDetails?

}