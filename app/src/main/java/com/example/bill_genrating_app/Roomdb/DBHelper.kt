package com.example.bill_genrating_app.Roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bill_genrating_app.Roomdb.DAO.itemDao
import com.example.bill_genrating_app.Roomdb.entities.items

@Database(entities = [items::class], version = 1)
abstract class DBHelper :RoomDatabase(){
    abstract fun itemDao(): itemDao
}