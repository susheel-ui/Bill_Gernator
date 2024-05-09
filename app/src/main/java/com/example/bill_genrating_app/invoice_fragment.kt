package com.example.bill_genrating_app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.example.bill_genrating_app.databinding.FragmentClientsFragmentsBinding
import com.example.bill_genrating_app.databinding.FragmentInvoiceFragmentBinding
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class invoice_fragment() : Fragment() {
    // TODO: Rename and change types of parameters
   lateinit var fragmentsBinding: FragmentInvoiceFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentsBinding = FragmentInvoiceFragmentBinding.inflate(layoutInflater);
// for demo class list
        val orderList = arrayOf(orders_entity(12345,100.00,true, Date()),
            orders_entity(65234567,100.00,true, Date())
        ,orders_entity(456765456,100.00,true, Date())
        ,orders_entity(345677654,100.00,true, Date())
        ,orders_entity(9876545678,100.00,true, Date()))
// TODO:: adding the pages
        var adapter = MyOrderListAdapter(requireActivity(),orderList)
        fragmentsBinding.ordersListsview.adapter = adapter

//        return inflater.inflate(R.layout.fragment_invoice_fragment, container, false)
        return fragmentsBinding.root
    }



}