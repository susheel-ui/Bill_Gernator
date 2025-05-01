package com.example.bill_genrating_app.entity

import java.util.Date
// this class is temporary for design the ui
class orders_entity(order_id:Long, GrandTotal:Double, status:Boolean, Date:Date) {
        var orderid:Long = order_id
            get() {
                return field;
            }
        var GrandTotal:Double = GrandTotal
            get() {
                return field
            }
}