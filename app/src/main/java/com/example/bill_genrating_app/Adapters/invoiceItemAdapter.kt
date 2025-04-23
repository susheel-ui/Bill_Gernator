package com.example.bill_genrating_app.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.entity.invoiceItem

class invoiceItemAdapter(private val itemList: ArrayList<invoiceItem>): RecyclerView.Adapter<invoiceItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
            val itemName:TextView
            val MRP: TextView
            val discountRate:TextView
            val quantity:TextView
            val total:TextView
            init {
                itemName =   itemView.findViewById(R.id.invoice_item_row_itemName)
                MRP = itemView.findViewById(R.id.invoice_item_row_MRP)
                discountRate = itemView.findViewById(R.id.invoice_item_row_discountRate)
                quantity = itemView.findViewById(R.id.invoice_item_row_Quantity)
                total = itemView.findViewById(R.id.invoice_item_row_total)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.invoice_item_row_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

           holder.itemName.text = item.name
           holder.MRP.text = item.MRP.toString()
           holder.discountRate.text = item.discount.toString()
           holder.quantity.text = item.quantity.toString()
           holder.total.text = item.total.toString()

    }


}