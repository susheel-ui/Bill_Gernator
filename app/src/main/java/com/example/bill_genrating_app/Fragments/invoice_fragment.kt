package com.example.bill_genrating_app.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bill_genrating_app.databinding.FragmentInvoiceFragmentBinding
import com.example.bill_genrating_app.entity.orders_entity
import java.util.Date

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
        val orderList = arrayOf(
            orders_entity(12345, 100.00, true, Date()),
            orders_entity(65234567, 100.00, true, Date())
        , orders_entity(456765456, 100.00, true, Date())
        , orders_entity(345677654, 100.00, true, Date())
        , orders_entity(9876545678, 100.00, true, Date())
        )
// TODO:: adding the pages
//        var adapter = MyOrderListAdapter(this,orderList)
//        fragmentsBinding.ordersListsview.adapter = adapter



        print(orderList)

//        return inflater.inflate(R.layout.fragment_invoice_fragment, container, false)
        return fragmentsBinding.root
    }

    public fun print(arr:Array<orders_entity>){
        for (x in arr){
            Log.d(ContentValues.TAG, "print: " + x.orderid)
        }
    }



}