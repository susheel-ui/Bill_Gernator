package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bill_genrating_app.Adapters.MyOrdersViewItemAdapter
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.databinding.ActivityOrdersBinding
import com.example.bill_genrating_app.viewModels.OrdersState
import com.example.bill_genrating_app.viewModels.OrdersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewOrdersActivity : AppCompatActivity() {
    lateinit var  binding :ActivityOrdersBinding
    lateinit var data : List<com.example.bill_genrating_app.Api.response.Order>
    lateinit var db: DBHelper
    lateinit var adapter:MyOrdersViewItemAdapter
    val orderViewModel: OrdersViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityOrdersBinding.inflate(layoutInflater)
       setContentView(binding.root)
        db = DBHelper.getDatabase(this)
        data = emptyList()
        adapter = MyOrdersViewItemAdapter(this, data)
        binding.ListOfOrderRV.adapter = adapter
        setData()
        orderViewModel.allOrderLiveData.observe(this){
            when(it){
                is OrdersState.Failed->{
                    Log.d(TAG, "onCreate: ${it.message}")
                }
                is OrdersState.Loading->{
                    Log.d(TAG, "onCreate: data Loading")
                }
                is OrdersState.Success->{
                    Log.d(TAG, "onCreate: Success ${it.data}")
                    data = it.data!!
                    adapter = MyOrdersViewItemAdapter(this, data)
                    binding.ListOfOrderRV.adapter = adapter
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun setData(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "setData: Coroutine Started")
//            data = db.orderDao().getAllOrders()
        }.invokeOnCompletion {
            Log.d(TAG, "setData: Coroutine Completed. Data size: ${data.size}")
            // Update the adapter on the main thread
            runOnUiThread {
                adapter = MyOrdersViewItemAdapter(this, data)
                binding.ListOfOrderRV.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
       setData()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay_static, R.anim.zoom_out)
    }

}