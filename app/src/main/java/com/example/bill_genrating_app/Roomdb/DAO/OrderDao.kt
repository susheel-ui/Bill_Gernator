package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bill_genrating_app.Roomdb.entities.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

    @Query("SELECT * FROM `Order`")
    suspend fun getAllOrders(): List<Order>
}
