package com.example.bill_genrating_app.Roomdb.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bill_genrating_app.Roomdb.entities.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun authenticateUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun checkUsernameExists(username: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Long?): User?
}