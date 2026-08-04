package com.example.bill_genrating_app.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bill_genrating_app.Api.response.Inventory
import com.example.bill_genrating_app.entity.invoiceItem
import java.text.DecimalFormat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.InvoiceItemRowLayoutBinding
import com.example.bill_genrating_app.entity.Invoice_item


class invoiceItemAdapter(
    private val itemList: ArrayList<Invoice_item>,
    private val smallTextFlag: Boolean = false,
    private val flagEditable: Boolean = true,
    private val onDataChanged: ((Pair<Int,String>) -> Unit)? = null
): RecyclerView.Adapter<invoiceItemAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //            lateinit var layout:InvoiceItemRowLayoutBinding
        val itemName: TextView
        val MRP: TextView
        val discountRate: TextView
        val quantity: TextView
        val total: TextView
        val moreOptionLayout: View
        val btnDelete: ImageButton
        val btnAdd: ImageButton
        val btnDec: ImageButton
        val SaveAmountTV: TextView

        init {
//                layout = LayoutInflater.from(itemView.context).inflate(R.layout.invoice_item_row_layout,null,false) as InvoiceItemRowLayoutBinding
            itemName = itemView.findViewById(R.id.invoice_item_row_itemName)
            MRP = itemView.findViewById(R.id.invoice_item_row_MRP)
            discountRate = itemView.findViewById(R.id.invoice_item_row_discountRate)
            quantity = itemView.findViewById(R.id.invoice_item_row_Quantity)
            total = itemView.findViewById(R.id.invoice_item_row_totalBig)
            moreOptionLayout = itemView.findViewById(R.id.moreOptionLayout)
            btnDelete = itemView.findViewById(R.id.invoice_item_row_btnDelete)
            btnAdd = itemView.findViewById(R.id.invoice_item_row_btnAdd)
            btnDec = itemView.findViewById(R.id.invoice_item_row_btnDec)
            SaveAmountTV = itemView.findViewById(R.id.savedItemAmount)
            if (!flagEditable) {
                btnAdd.visibility = View.GONE
                btnDec.visibility = View.GONE
                btnDelete.visibility = View.GONE
            }
            if (smallTextFlag) {
                itemName.textSize = 8f
                MRP.textSize = 8f
                discountRate.textSize = 8f
                quantity.textSize = 8f
                total.textSize = 8f
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.invoice_item_row_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slideDown: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_down)
        val slideUp: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_up)
        val item = itemList[position]
        val df = DecimalFormat("#,###." + "0".repeat(2))
        holder.itemName.text = item.name
        holder.MRP.text = item.initialMRP.toString()
        holder.discountRate.text = item.initialDiscount.toString()
        holder.quantity.text = item.initialQuantity.toString()
        holder.total.text = df.format(item.total)
        val total = (item.initialMRP*item.initialQuantity)-item.total
        holder.SaveAmountTV.text = total.toString();

//            holder.itemView.setOnClickListener {
//                if (holder.moreOptionLayout.visibility == View.VISIBLE){
//                    holder.moreOptionLayout.startAnimation(slideUp)
//                        holder.moreOptionLayout.visibility = View.GONE
//                }else{
//                    holder.moreOptionLayout.startAnimation(slideDown)
//                    holder.moreOptionLayout.visibility = View.VISIBLE
//
//                }
//            }
        holder.btnAdd.setOnClickListener {
//            item.initialQuantity = item.initialQuantity + 1
//            val discountedPrice = item.initialMRP * (1 - (item.initialDiscount / 100.0))
//            item.total = item.initialQuantity * discountedPrice
//            holder.quantity.text = item.initialQuantity.toString()
//            holder.total.text = df.format(item.total)
            onDataChanged?.invoke(Pair(item.id, "Add"))

        }
        holder.btnDec.setOnClickListener {
            if (item.initialQuantity > 1) {
//                item.initialQuantity = item.initialQuantity - 1
//                val discountedPrice = item.initialMRP * (1 - (item.initialDiscount / 100.0))
//                item.total = item.initialQuantity * discountedPrice
//                holder.quantity.text = item.initialQuantity.toString()
//                holder.total.text = df.format(item.total)
            }
            onDataChanged?.invoke(Pair(item.id, "Dec"))
        }
        holder.btnDelete.setOnClickListener {
//            itemList.removeAt(position)
            onDataChanged?.invoke(Pair(item.id, "Del"))
        }
    }
}








