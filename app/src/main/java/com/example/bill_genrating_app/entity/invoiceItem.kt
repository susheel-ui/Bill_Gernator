package com.example.bill_genrating_app.entity

data class invoiceItem(val barCodeId:Long, val name:String?, val initialMRP:Double, var initialQuantity:Int, val initialDiscount:Double, var total: Double)