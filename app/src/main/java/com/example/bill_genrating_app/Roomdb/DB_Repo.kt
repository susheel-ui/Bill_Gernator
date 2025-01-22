package com.example.bill_genrating_app.Roomdb

import com.example.bill_genrating_app.Roomdb.entities.items

class DB_Repo(private val db:DBHelper) {
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