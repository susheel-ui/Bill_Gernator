package com.example.bill_genrating_app.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.databinding.ItemShowListLayoutBinding

class AdapterItems(var context:Context,var listItem:List<items>) :RecyclerView.Adapter<AdapterItems.ViewHolder>() {
    class ViewHolder(view : View) :RecyclerView.ViewHolder(view){
        val itemId:TextView
        val name :TextView
        val quntity:TextView

//        val ItemShowBinding:ItemShowListLayoutBinding
        init {
//            ItemShowBinding = ItemShowListLayoutBinding.inflate(LayoutInflater.from())
           itemId = view.findViewById(R.id.itemShowItemId)
            name = view.findViewById(R.id.itemShowItemName)
            quntity = view.findViewById(R.id.itemShowQuntityTextField)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterItems.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_list_layout,parent,false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterItems.ViewHolder, position: Int) {
        val item:items = listItem.get(position)
        holder.itemId.text = item.BarcodeId.toString()
        holder.name.text = item.Name.plus(item.weight.plus(item.weightType))
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}