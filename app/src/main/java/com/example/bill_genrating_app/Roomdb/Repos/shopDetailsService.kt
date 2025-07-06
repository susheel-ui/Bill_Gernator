package com.example.bill_genrating_app.Roomdb.Repos

import android.content.Context
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import kotlinx.coroutines.CoroutineScope

class shopDetailsService(private val context:Context) {
    val db = DBHelper.getDatabase(context)

    suspend fun insertShopDetails(shopDetails: shopDetails) {
        db.shopDetailsDao().insertShopDetails(shopDetails)
    }

    suspend fun updateShopDetails(shopDetails: shopDetails) {
        db.shopDetailsDao().updateShopDetails(shopDetails)
    }

    suspend fun getShopDetails(id: Int): shopDetails? {
        return db.shopDetailsDao().getShopDetails(id)
    }

}