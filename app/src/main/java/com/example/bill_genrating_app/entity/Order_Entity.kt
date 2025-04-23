package com.example.bill_genrating_app.entity

class Order_Entity(Name:String,Mobile:String,GrandTotal:Double,items:ArrayList<invoiceItem>,status:Boolean){
        val Name = Name
            get(){
                return field
            }
        val Mobile = Mobile
            get ()  {
                return field
            }
        val GrandTotal = GrandTotal
            get(){
                return field
            }
        val item:ArrayList<invoiceItem> = items
            get(){
                return field
            }
        val status = status
            get(){
                return field
            }
}