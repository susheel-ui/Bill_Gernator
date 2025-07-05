package com.example.bill_genrating_app.entity

class invoiceItem(barCodeId:Long,name:String,initialMRP:Double,initialQuantity:Int,initialDiscount:Double) {

        var total : Double = 0.0
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
        var quantity = initialQuantity
            get()  {
                return field
            }
            set(q:Int) {
                field = q
                calculateTotal()
            }
    var MRP = initialMRP
        get(){
            return field
        }
        set(value) {
            field = value
            calculateTotal()
        }
    var discount = initialDiscount
        get() {
            return field
        }
        set(value) {
            field = value
            calculateTotal()
        }
   private fun calculateTotal() {
       total = MRP * quantity - MRP * quantity * (discount / 100)
   }
    init {
        calculateTotal()
    }
}