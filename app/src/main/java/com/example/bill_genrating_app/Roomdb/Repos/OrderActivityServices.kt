package com.example.bill_genrating_app.Roomdb.Repos

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Transaction
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem

class OrderActivityServices(private val context:Context) {
    val db:DBHelper
        init {
             db = DBHelper.getDatabase(context)
        }
    @Transaction
        public suspend fun saveToDb(order: Order, orderItems: List<OrderItem>){
        try {
            db.orderDao().insert(order)
            Log.d(TAG, "saveToDB: order saved successful")
        }catch (e:Exception){
            Log.d(TAG, "saveToDB: Duplicate error ${e.message}")
            db.orderDao().update(order)
        }

        orderItems.forEach { ordItem ->
            try{
                db.orderItemDao().insert(ordItem)
                Log.d(TAG, "saveToDB: successful ordered item save")
            }catch (e:Exception){
                Log.d(TAG, "saveToDB: Duplicate error ${e.message}")
                db.orderItemDao().update(ordItem)
            }finally {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
        }
}