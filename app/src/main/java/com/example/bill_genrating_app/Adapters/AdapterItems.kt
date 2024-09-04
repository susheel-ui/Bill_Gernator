package com.example.bill_genrating_app.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.Activities.ViewItemActivity

class AdapterItems(var mContext:Context, var listItem:List<items>) :RecyclerView.Adapter<AdapterItems.ViewHolder>() {
    inner class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val itemId:TextView
        val name :TextView
        val quntity:TextView

//        val ItemShowBinding:ItemShowListLayoutBinding
        init {
//            ItemShowBinding = ItemShowListLayoutBinding.inflate(LayoutInflater.from())
           itemId = itemView.findViewById(R.id.itemShowItemId)
            name = itemView.findViewById(R.id.itemShowItemName)
            quntity = itemView.findViewById(R.id.itemShowQuntityTextField)

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
        holder.quntity.text = item.stockQuantity.toString()
        holder.itemView.setOnClickListener {
            Toast.makeText(mContext, "touch = ${item.Name}", Toast.LENGTH_SHORT).show()
           try {
               var intent = Intent(mContext, ViewItemActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.putExtra("itemId",item.BarcodeId.toString())
               mContext.startActivity(intent)
           }
           catch (e:Exception){
               Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show()
               Log.d("Error in onclick", "onBindViewHolder: ${e.message}")
           }

        }

    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}