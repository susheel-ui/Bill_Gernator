package com.example.bill_genrating_app.Adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.UtilClasses.ExtractDateFromOrdID
import com.example.bill_genrating_app.UtilClasses.formatDateTime
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.databinding.OrdersListLayoutBinding
import java.util.Locale

class HistoryOrderAdapter(
    private var orders: List<Order>,
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: OrdersListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(order: Order) {
            binding.OrderNameField.text = order.clientName.uppercase(Locale.getDefault())
            binding.priceTag.text = "₹${order.finalPrice}"
            binding.statusTag.text = order.paymentStatus
            
            when (order.paymentStatus) {
                status.PAID.toString() -> {
                    binding.statusTag.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green))
                }
                status.PENDING.toString() -> {
                    binding.statusTag.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                }
                else -> {
                    binding.statusTag.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorBlue))
                }
            }

            try {
                binding.date.text = formatDateTime(order.createdAt)
            } catch (e: Exception) {
                binding.date.text = "N/A"
            }

            binding.root.setOnClickListener { onItemClick(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrdersListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
