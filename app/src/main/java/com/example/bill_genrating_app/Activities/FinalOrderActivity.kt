package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Fragments.Fragment_Invoice_billingItems
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem
import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding
import com.example.bill_genrating_app.entity.invoiceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FinalOrderActivity : AppCompatActivity() {
    lateinit var  activityBinding: ActivityFinalOrderBinding
    lateinit var db:DBHelper;
    lateinit var orderData: Order
    lateinit var ItemList:List<OrderItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val OrderId = intent.getStringExtra("OrderId")
        db = DBHelper.getDatabase(this);
//        Log.d(TAG, "onCreate:  $OrderId")   // for only testing purpose and working propper tested


    lifecycleScope.launch {
        val OrderData = async {
            getData(OrderId.toString())
        }
        val data = OrderData.await() as Order

        val job2 = async {
            orderData = data
            getItemList(data.ordId.toString())
        }
        val result = job2.await()
        ItemList = result as List<OrderItem>

    }.invokeOnCompletion {
        activityBinding.usernameText.text = orderData.name +"( +91-${orderData.mob} )"
        activityBinding.balanceText.text = "\u20B9"+orderData.grandTotal.toString()

    }




    }
    private suspend fun getData(ordId:String):Order{
      return db.orderDao().findById(ordId)!!
    }
    private suspend fun getItemList(OrderId:String):List<OrderItem>{
            return db.orderItemDao().findByOrderId(OrderId.toString())
    }

    private fun setOrderItems(list:List<invoiceItem>){
        val fragment = Fragment_Invoice_billingItems(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}