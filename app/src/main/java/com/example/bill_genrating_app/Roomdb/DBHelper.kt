package com.example.bill_genrating_app.Roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bill_genrating_app.Roomdb.DAO.itemDao
import com.example.bill_genrating_app.Roomdb.entities.items

@Database(entities = [items::class], version = 1)
abstract class DBHelper :RoomDatabase(){
    abstract fun itemDao(): itemDao

    //getInstance
    companion object{
        fun getInstance(context: Context):DBHelper{
            var db :DBHelper? = null
            if(db== null){
                return Room.databaseBuilder(context, DBHelper::class.java,"DatabaseBillGenerator")
                    .allowMainThreadQueries()
                    .build()
            }
            return db
        }
    }

}