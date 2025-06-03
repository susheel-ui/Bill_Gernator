package com.example.bill_genrating_app.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bill_genrating_app.Activities.FinalOrderActivity
import com.example.bill_genrating_app.databinding.OrdersListLayoutBinding
import com.example.bill_genrating_app.Fragments.invoice_fragment
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.UtilClasses.ExtractDateFromOrdID
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding
import java.util.Locale

class MyOrdersViewItemAdapter(val context: Context, val arr:List<Order>) : BaseAdapter(){
    override fun getCount(): Int {
        return arr.size;
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @SuppressLint("ResourceAsColor")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertview = OrdersListLayoutBinding.inflate(LayoutInflater.from(context))
        var entity = arr.get(p0)
        convertview.OrderNameField.text = entity.name.toString().uppercase(Locale.getDefault())
        convertview.priceTag.text = "\u20B9"+entity.grandTotal.toString()

        when (entity.status) {
            status.PAID.toString() -> {
                convertview.priceTag.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            status.PENDING.toString() -> convertview.priceTag.setTextColor(ContextCompat.getColor(context, R.color.red))
            else -> convertview.priceTag.setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
        }
        val date  = ExtractDateFromOrdID(entity.ordId.toString())
        convertview.date.text = date
        convertview.root.setOnClickListener {
            val intent = Intent(context, FinalOrderActivity::class.java)
            intent.putExtra("OrderId",entity.ordId);
            context.startActivity(intent)
        }
        return convertview.root

    }


}