package com.example.bill_genrating_app.Roomdb.Repo

import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.User

// user Repo Functions
class user_Repo(private val db:DBHelper) {
    val userDao  = db.UserDao()
    // login Function
    fun LoginUsr(usrID:String,psw:String): List<User>{
        return userDao.login(usrID,psw)
    }
    // function for User
    suspend fun resisterUser(user:User){
        userDao.updateUser(user)
    }
    suspend fun updateUser(user:User){
        userDao.updateUser(user)
    }

}