package com.example.bill_genrating_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.bill_genrating_app.databinding.OrdersListLayoutBinding

class MyCostomAdapter(val context: invoice_fragment, val arr:Array<orders_entity>) : BaseAdapter(){
    override fun getCount(): Int {
        return arr.size;
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertview = OrdersListLayoutBinding.inflate(LayoutInflater.from(context.requireActivity()))

        var entity = arr.get(p0)
        convertview.orderNo.text = entity.orderid.toString()
        convertview.priceTag.text = entity.Total.toString()

        return convertview.root

    }

}