package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bill_genrating_app.Roomdb.entities.Order

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order)

    @Query("SELECT * FROM `Order`")
    suspend fun getAllOrders(): List<Order>

    @Query("SELECT * FROM `Order` WHERE ordId = :orderId")
    suspend fun findById(orderId: String): Order?

    @Query("UPDATE `Order` SET status = :status WHERE ordId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Update
    suspend fun update(order: Order)
}
