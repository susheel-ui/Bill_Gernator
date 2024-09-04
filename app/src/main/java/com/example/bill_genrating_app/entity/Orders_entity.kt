package com.example.bill_genrating_app.entity

import java.util.Date
// this class is temporary for design the ui
class orders_entity(order_id:Long,total:Double,status:Boolean,Date:Date) {
        var orderid:Long = order_id
            get() {
                return field;
            }
        var Total:Double = total
            get() {
                return field
            }

}