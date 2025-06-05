package com.example.bill_genrating_app.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bill_genrating_app.Adapters.invoiceItemAdapter

import com.example.bill_genrating_app.databinding.FragmentInvoiceBillingItemsBinding
import com.example.bill_genrating_app.entity.invoiceItem

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_Invoice_billingItems.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_Invoice_billingItems(val data: List<invoiceItem>) : Fragment() {
    lateinit var fragmentBinding: FragmentInvoiceBillingItemsBinding
    lateinit var adapter: invoiceItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentInvoiceBillingItemsBinding.inflate(inflater)
        adapter = invoiceItemAdapter(data as ArrayList<invoiceItem>)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentBinding.itemRecycleView.layoutManager = layoutManager
        fragmentBinding.itemRecycleView.adapter = adapter
        return fragmentBinding.root
    }

}