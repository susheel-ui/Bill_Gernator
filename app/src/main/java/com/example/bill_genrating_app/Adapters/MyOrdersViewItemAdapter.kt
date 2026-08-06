package com.example.bill_genrating_app.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.bill_genrating_app.Activities.FinalOrderActivity
import com.example.bill_genrating_app.databinding.OrdersListLayoutBinding
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.UtilClasses.ExtractDateFromOrdID
import com.example.bill_genrating_app.UtilClasses.formatDateTime
import com.example.bill_genrating_app.UtilClasses.status
import java.util.Locale


class MyOrdersViewItemAdapter(val context: Context, private val arr: List<Order>) : BaseAdapter(){
    override fun getCount(): Int {
        return arr.size;
    }

    override fun getItem(position: Int): Any {
        return arr[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding: OrdersListLayoutBinding
        val convertView: View

        if (p1 == null) {
            binding = OrdersListLayoutBinding.inflate(LayoutInflater.from(context), p2, false)
            convertView = binding.root
            convertView.tag = binding
        } else {
            convertView = p1
            binding = convertView.tag as OrdersListLayoutBinding
        }

        val entity = arr[p0]
        binding.OrderNameField.text = entity.clientName.toString().uppercase(Locale.getDefault())
        binding.priceTag.text = "\u20B9${entity.totalMoney}"
        when (entity.paymentStatus) {
            status.PAID.toString() -> {
                binding.statusTag.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.statusTag.text = status.PAID.toString()
            }
            status.PENDING.toString() -> {
                binding.statusTag.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.statusTag.text = status.PENDING.toString()
            }
            else -> binding.statusTag.setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
        }
        binding.date.text = formatDateTime( entity.createdAt)
        binding.root.setOnClickListener {
            val intent = Intent(context, FinalOrderActivity::class.java)
            intent.putExtra("OrderId", entity.id.toString())
            context.startActivity(intent)
        }
        return convertView
    }
}