package com.example.bill_genrating_app.Roomdb.Repo

import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.items

class item_Repo(db: DBHelper) {
        private val itemDao = db.itemDao()
    fun getItems():List<items>{
        return itemDao.getall()
    }
   suspend fun SaveItem(item:items){
       itemDao.SaveNewItem(item)
    }

    suspend fun DeleteItem(item:items){
       itemDao.DeleteItem(item)
    }
    fun getByName(str:String):List<items>{
        return itemDao.getByname(str)
    }
    fun getById(id:Long):List<items>{
       return itemDao.getByid(id)
    }
    suspend fun updateItemsQuantity(stockQuantity:Long,barcodeId:Long){
        itemDao.updateItemsQuantity(stockQuantity,barcodeId)
    }




}