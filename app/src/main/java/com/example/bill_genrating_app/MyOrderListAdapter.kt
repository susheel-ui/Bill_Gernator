package com.example.bill_genrating_app

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.bill_genrating_app.databinding.OrdersListLayoutBinding

class MyOrderListAdapter(private val context:Activity,private val list:Array<orders_entity>) :ArrayAdapter<String>(context,R.layout.orders_list_layout){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        //TODO::here is error (for solve next day we will work
        val layoutInflater =context.layoutInflater
        val binding_view = layoutInflater.inflate(R.layout.orders_list_layout,null,true)


        val entity = list.get(position)
        val orderview = binding_view.findViewById(R.id.orderNo) as TextView
        val total = binding_view.findViewById<TextView>(R.id.priceTag)
            orderview.text = entity.orderid.toString()
            total.text = entity.Total.toString()
        return binding_view

    }
}