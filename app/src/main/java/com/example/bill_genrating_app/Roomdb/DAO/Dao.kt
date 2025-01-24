package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bill_genrating_app.Roomdb.entities.User
import com.example.bill_genrating_app.Roomdb.entities.items

// Dao for items
@Dao
interface itemDao {
    @Query("Select * from items")
    fun getall(): List<items>

    @Query("SELECT * FROM ITEMS WHERE BarcodeId=(:barcodeId)")
    fun getByid(barcodeId: Long): List<items>

    @Insert
    suspend fun SaveNewItem(vararg items: items)

    @Delete
    suspend fun DeleteItem(items: items)

    @Query("SELECT * FROM ITEMS WHERE Name Like '%'||:str||'%'")
    fun getByname(str: String): List<items>

    //Update the item by id
    @Query("UPDATE ITEMS SET stockQuantity = :stockQuantity where BarcodeId=(:barcodeId);")
    suspend fun updateItemsQuantity(stockQuantity: Long, barcodeId: Long)
}

//  Doo for User
@Dao
interface UserDao {
    @Query("SELECT * FROM Users WHERE UserId=(:userId) AND Password=(:psw)")
    fun login(userId: String, psw: String): List<User>

    @Insert
    suspend fun resister(user: User)

    @Update
    suspend fun updateUser(user: User)
}