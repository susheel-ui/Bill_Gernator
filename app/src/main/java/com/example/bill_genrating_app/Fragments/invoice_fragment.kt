package com.example.bill_genrating_app.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bill_genrating_app.Activities.ViewOrdersActivity
import com.example.bill_genrating_app.Adapters.MyOrdersViewItemAdapter
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.databinding.FragmentInvoiceFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class invoice_fragment() : Fragment() {
    // TODO: Rename and change types of parameters
   lateinit var fragmentsBinding: FragmentInvoiceFragmentBinding
   lateinit var db:DBHelper
   lateinit var orderData:List<Order>
   lateinit var adapter:MyOrdersViewItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentsBinding = FragmentInvoiceFragmentBinding.inflate(layoutInflater);

        //All Click listeners
            //see all click listener
                fragmentsBinding.fragmentSeeAllTag.setOnClickListener {
                   val intent = Intent(requireContext(), ViewOrdersActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.zoom_in,R.anim.stay_static)
                }

        //data getting
        ShowTransactions()
        return fragmentsBinding.root
    }

    override fun onResume() {
        super.onResume()
    }
    private suspend fun getData(): List<Order>{
        return db.orderDao().getAllOrders()
    }

    private fun ShowTransactions(){
        var data:List<Order> = listOf()
        val job1 = CoroutineScope(Dispatchers.IO).launch{
            data = getData().reversed();
        }.invokeOnCompletion {
            adapter = MyOrdersViewItemAdapter(requireContext(), data.subList(0,3))
            fragmentsBinding.ordersListsview.adapter = adapter
        }



    }



}