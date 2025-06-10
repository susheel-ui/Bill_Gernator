package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bill_genrating_app.Adapters.MyOrdersViewItemAdapter
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.databinding.ActivityOrdersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewOrdersActivity : AppCompatActivity() {
    lateinit var  binding :ActivityOrdersBinding
    lateinit var data :List<Order>
    lateinit var db: DBHelper
    lateinit var adapter:MyOrdersViewItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityOrdersBinding.inflate(layoutInflater)
       setContentView(binding.root)
        db = DBHelper.getDatabase(this)
        data = listOf()
        adapter = MyOrdersViewItemAdapter(this, data.reversed())
        binding.ListOfOrderRV.adapter = adapter
       setData()

    }
    @OptIn(DelicateCoroutinesApi::class)
    fun setData(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "setData: Coroutine Started")
            data = db.orderDao().getAllOrders()
        }.invokeOnCompletion {
            Log.d(TAG, "setData: Coroutine Completed. Data size: ${data.size}")
            // Update the adapter on the main thread
            runOnUiThread {
                adapter = MyOrdersViewItemAdapter(this, data.reversed())
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