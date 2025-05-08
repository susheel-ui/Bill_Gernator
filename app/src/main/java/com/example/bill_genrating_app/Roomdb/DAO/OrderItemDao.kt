package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bill_genrating_app.Roomdb.entities.OrderItem

@Dao
interface OrderItemDao {
    @Insert
    suspend fun insert(vararg orderItem: OrderItem)

    @Query("SELECT * FROM OrderItem")
    suspend fun getAllOrderItems(): List<OrderItem>

    @Update
    suspend fun update(orderItem: OrderItem)

    @Delete
    suspend fun delete(orderItem: OrderItem)

    @Query("DELETE FROM OrderItem where ordId = :orderId")
    suspend fun deleteById(orderId: String)


}
