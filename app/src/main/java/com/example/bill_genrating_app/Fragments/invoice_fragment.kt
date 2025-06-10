package com.example.bill_genrating_app.Fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.withContext

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
        ShowTransactions()
    }
      private suspend fun getData(): List<Order>{
        return db.orderDao().getAllOrders()
    }

    private fun ShowTransactions(){
        var data:List<Order> = listOf()
        val job1 = CoroutineScope(Dispatchers.IO).launch{
            data = getData().reversed();
        }.invokeOnCompletion {
            Log.d(TAG, "ShowTransactions: dataset initialised")
            // Ensure the fragment is still attached to an activity and context is available
            if (isAdded && context != null) {
               requireActivity().runOnUiThread{
                    // Check if data has enough elements before creating a subList
                    val itemsToShow = if (data.size >= 3) data.subList(0, 3) else data
                    adapter = MyOrdersViewItemAdapter(requireContext(), itemsToShow)
                    fragmentsBinding.ordersListsview.adapter = adapter
                }
            }
        }



    }



}