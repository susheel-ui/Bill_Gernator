package com.example.bill_genrating_app.Roomdb.Repos

import android.content.Context
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.User

class UserService(private val context:Context) {
    val db :DBHelper = DBHelper.getDatabase(context)
    suspend fun registerUser(user: User): Long {
        return db.userDao().registerUser(user)
    }
  fun authenticateUser(email: String, password: String):User? {
        val user = db.userDao().authenticateUser(email, password)
        return user
    }
   fun getUserById(id: Long?): User? {
        return db.userDao().getUserById(id)
    }
}