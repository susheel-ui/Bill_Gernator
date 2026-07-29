package com.example.bill_genrating_app.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bill_genrating_app.Activities.LoginActivity
import com.example.bill_genrating_app.Activities.ViewOrdersActivity
import com.example.bill_genrating_app.Adapters.MyOrdersViewItemAdapter
import com.example.bill_genrating_app.Api.response.Metrices
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.User
import com.example.bill_genrating_app.UtilClasses.change_fragment
import com.example.bill_genrating_app.databinding.FragmentInvoiceFragmentBinding
import com.example.bill_genrating_app.viewModels.HomeViewModel
import com.example.bill_genrating_app.viewModels.MetriceState
import com.example.bill_genrating_app.viewModels.OrdersState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class invoice_fragment() : Fragment() {
    // TODO: Rename and change types of parameters
   lateinit var fragmentsBinding: FragmentInvoiceFragmentBinding
   lateinit var db:DBHelper
   lateinit var orderData: List<com.example.bill_genrating_app.Api.response.Order>
   lateinit var adapter:MyOrdersViewItemAdapter
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper.getDatabase(requireContext())
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentsBinding = FragmentInvoiceFragmentBinding.inflate(layoutInflater);

        //All Click listeners
            //see all click listener
                fragmentsBinding.fragmentSeeAllTag.setOnClickListener {
//                   val intent = Intent(requireContext(), ViewOrdersActivity::class.java)
//                    startActivity(intent)
//                    requireActivity().overridePendingTransition(R.anim.zoom_in,R.anim.stay_static)

                       }

            //fragmentsBinding.invoiceSearchbar.emailEt.text  = user?.username.toString()
        //data getting
//        ShowTransactions()



        return fragmentsBinding.root
    }

    private fun setMetrices(data: Metrices) {
        fragmentsBinding.incomeAmount.text = data.todayTotalIncome.toString()
        fragmentsBinding.pendingCount.text = data.pendingOrdersCount.toString()
        fragmentsBinding.invoiceCount.text = data.totalInvoicesCount.toString()
        fragmentsBinding.lowStockCount.text = data.lowStocksCount.toString()
        fragmentsBinding.inventoryCount.text = data.totalInventoriesCount.toString()
        fragmentsBinding.increamentPercentage.text = data.incomeIncrementPercentage.toString().plus("%")

    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshUi()

        viewModel.recentTransactionLiveData.observe(this){
            when(it){
                is OrdersState.Failed->{
                    Log.d(TAG, "onCreateView: ${it.message}")
                    if(it.code == 403){
//                        viewModel.sharedPreferences.logout(true)
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                }
                is OrdersState.Loading->{
                    Log.d(TAG, "onCreateView: data Loading")
                }
                is OrdersState.Success->{
                    Log.d(TAG, "onCreateView: Success ${it.data}")
                    orderData = it.data!!
                    adapter = MyOrdersViewItemAdapter(requireContext(), orderData)
                    fragmentsBinding.ordersListsview.adapter = adapter
                }
            }
        }
        viewModel.metricesLiveData.observe(this){
            when(it){
                is MetriceState.Failed->{
                    Log.d(TAG, "onCreateView: ${it.message}")
                }
                is MetriceState.Loading->{
                    Log.d(TAG, "onCreateView: data Loading")
                }
                is MetriceState.Success->{
                    Log.d(TAG, "onCreateView: Success ${it.data}")
                    setMetrices(it.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ShowTransactions()
        viewModel.refreshUi()
    }
      private suspend fun getData(): List<Order>{
        return db.orderDao().getAllOrders()

    }

    private fun ShowTransactions(){
        var data:List<Order> = listOf()
        val job1 = CoroutineScope(Dispatchers.IO).launch{
            data = getData().reversed();
        }.invokeOnCompletion {
//            Log.d(TAG, "ShowTransactions: dataset initialised")
            // Ensure the fragment is still attached to an activity and context is available
            if (isAdded && context != null) {
               requireActivity().runOnUiThread{
                    // Check if data has enough elements before creating a subList
//                    val itemsToShow = if (data.size >= 3) data.subList(0, 3) else data
//                    adapter = MyOrdersViewItemAdapter(requireContext(), itemsToShow)
//                    fragmentsBinding.ordersListsview.adapter = adapter
                }
            }
        }
    }
}