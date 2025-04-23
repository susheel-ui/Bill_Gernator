package com.example.bill_genrating_app.entity

class invoiceItem(barCodeId:Long,name:String,MRP:Double,quantity:Int,discount:Double) {
        var total = MRP - MRP * (discount / 100)
            get(){
                return field
            }
        var name = name
            get(){
                return field
            }
        var barCodeId = barCodeId
            get(){
                return field
            }
        var quantity = quantity
            get()  {
                return field
            }
            set(q:Int) {
                field = q
            }
    var MRP = MRP
        get(){
            return field
        }
    var discount = discount
        get() {
            return field
        }
}