package com.example.bill_genrating_app.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bill_genrating_app.Adapters.invoiceItemAdapter

import com.example.bill_genrating_app.databinding.FragmentInvoiceBillingItemsBinding
import com.example.bill_genrating_app.entity.invoiceItem
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Invoice_billingItems.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Invoice_billingItems() : Fragment() {
    lateinit var fragmentBinding: FragmentInvoiceBillingItemsBinding
    lateinit var adapter: invoiceItemAdapter
    var data:ArrayList<invoiceItem> = ArrayList()
    var GrandTotal:String = "0.0" // Initialize with a default value
    constructor(data: ArrayList<invoiceItem>, GrandTotal: String):this(){
        this.data = data
        this.GrandTotal = GrandTotal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentInvoiceBillingItemsBinding.inflate(inflater, container, false)
        adapter = invoiceItemAdapter(data) // Ensure data is initialized before adapter creation
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentBinding.itemRecycleView.layoutManager = layoutManager
        fragmentBinding.itemRecycleView.adapter = adapter
        // GrandTotal is initialized in the constructor or with a default value, so no need for ::GrandTotal.isInitialized check
        fragmentBinding.GrandTotalTextView.text = "\u20B9" + GrandTotal
        lifecycleScope.launch {
            val result = calculateSavedMoney(GrandTotal.toDouble())
            val df = DecimalFormat("#,###." + "0".repeat(2))
            fragmentBinding.saveMoneyTV.text = "\u20B9".plus(df.format(result))
        }
        return fragmentBinding.root
    }

    private fun calculateSavedMoney(grandTotal: Double): Double {
        var MRP_GrandTotal: Double = 0.0;
        for (x in data) {
            MRP_GrandTotal += x.MRP * x.quantity;
        }
        return MRP_GrandTotal-grandTotal;
    }

}