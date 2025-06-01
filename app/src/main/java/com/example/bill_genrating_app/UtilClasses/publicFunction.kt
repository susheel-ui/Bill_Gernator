package com.example.bill_genrating_app.UtilClasses

import android.text.format.Formatter
import org.jetbrains.annotations.TestOnly

fun ExtractDateFromOrdID(OrdId:String):String{
   val date = OrdId.subSequence(3,11).toString()
    val year = date.subSequence(0,4).toString()
    val month = date.subSequence(4,6).toString()
    val day = date.subSequence(6,8).toString()
    return "$day/$month/$year";
}
enum class status{
    PENDING,PAID
}