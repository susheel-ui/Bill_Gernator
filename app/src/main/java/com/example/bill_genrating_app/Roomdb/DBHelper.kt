package com.example.bill_genrating_app.Roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bill_genrating_app.Roomdb.DAO.OrderDao
import com.example.bill_genrating_app.Roomdb.DAO.OrderItemDao
import com.example.bill_genrating_app.Roomdb.DAO.ShopDetailsDao
import com.example.bill_genrating_app.Roomdb.DAO.UserDao
import com.example.bill_genrating_app.Roomdb.DAO.itemDao
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem
import com.example.bill_genrating_app.Roomdb.entities.User
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import kotlinx.coroutines.CoroutineScope

@Database(entities = [items::class, Order::class, OrderItem::class, User::class,shopDetails::class], version = 1)
abstract class DBHelper :RoomDatabase(){
    abstract fun itemDao(): itemDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun userDao(): UserDao
    abstract fun shopDetailsDao():ShopDetailsDao
    companion object{
        @Volatile
        private var INSTANCE: DBHelper? = null
        fun getDatabase(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "DatabaseBillGenerator"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}