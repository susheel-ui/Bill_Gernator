package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bill_genrating_app.Roomdb.entities.OrderItem

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderItem: OrderItem)

    @Query("SELECT * FROM OrderItem")
    suspend fun getAllOrderItems(): List<OrderItem>
}
