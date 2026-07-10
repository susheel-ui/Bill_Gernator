package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG

import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Adapters.invoiceItemAdapter
import com.example.bill_genrating_app.Api.response.Inventory

import com.example.bill_genrating_app.R

import com.example.bill_genrating_app.Roomdb.Repos.OrderActivityServices

import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding

import kotlinx.coroutines.launch

import com.example.bill_genrating_app.Api.response.Order
import com.example.bill_genrating_app.Api.response.OrderItem
import com.example.bill_genrating_app.Payment_Options_Activity
import com.example.bill_genrating_app.UtilClasses.formatDateTime
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.entity.invoiceItem
import com.example.bill_genrating_app.viewModels.Factories.ViewOrderViewFactory
import com.example.bill_genrating_app.viewModels.OrderState
import com.example.bill_genrating_app.viewModels.ViewOrderViewModel
import kotlin.text.clear

class FinalOrderActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityFinalOrderBinding

    lateinit var orderData: Order
     val orderItems = ArrayList<Inventory>()
    lateinit var launcherActivity: ActivityResultLauncher<Intent>
    lateinit var invoiceItemAdapter: invoiceItemAdapter

    val viewModel: ViewOrderViewModel by viewModels {
        ViewOrderViewFactory(application, intent.getStringExtra("OrderId")!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        val OrderId = intent.getStringExtra("OrderId")

        Log.d(TAG, "onCreate: Order : $OrderId")


        launcherActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
//                updateUIForPaidStatus()
                lifecycleScope.launch {
                    OrderActivityServices(applicationContext).updateOrderStatus(
                        orderData.id.toString(),
                        status.PAID.toString()
                    )
                }
            }
        }


        viewModel.orderLiveData.observe(this) {
            when (it) {
                is OrderState.Loading -> {
                    Log.d(TAG, "onCreate: Order Loading ")
                }
                is OrderState.Success -> {
                    Log.d(TAG, "onCreate: Order Success${it.data} ")
                    orderData = it.data
                    setuserData()
                    setInvoicesItem(it.data.Order_items)
                }
                is OrderState.Failed -> {
                    Log.d(TAG, "onCreate: Order Failed  ${it.message}")
                }
            }
        }

        activityBinding.btnNextProcess.setOnClickListener {
            when (orderData.paymentStatus) {
                status.PAID.toString() -> {

                }

                status.PENDING.toString() -> {
                    val intent = Intent(this, Payment_Options_Activity::class.java).apply {
                        putExtra("OrderId", orderData.id.toString())
                    }
                    launcherActivity.launch(intent)
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setuserData() {
        activityBinding.customerName.text = orderData.clientName
        activityBinding.orderId.text = orderData.id.toString()
//        activityBinding.customerEmail.text = orderData.clientEmail
//        activityBinding.customerPhone.text = orderData.
        activityBinding.totalAmountValue.text = orderData.finalPrice.toString()
        activityBinding.tvGrandTotalSummary.text = orderData.finalPrice.toString()
        activityBinding.statusChip.text = orderData.paymentStatus
        when (orderData.paymentStatus) {
            status.PAID.toString() -> {
                activityBinding.statusChip.setTextColor(ContextCompat.getColor(this, R.color.green))
                activityBinding.btnNextProcess.text = "Print Bill"
            }

            status.PENDING.toString() -> {
                activityBinding.statusChip.setTextColor(ContextCompat.getColor(this, R.color.red))
                activityBinding.btnNextProcess.text = "Make Payment"
            }

            else -> activityBinding.statusChip.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorBlue
                )
            )
        }
        activityBinding.tvPaymentMode.text = orderData.paymentMode
        activityBinding.tvDueDate.text = formatDateTime(orderData.createdAt)
        activityBinding.tvSubtotal.text = orderData.totalMoney.toString()

    }
    private fun setInvoicesItem(x: List<OrderItem>) {
        orderItems.clear()
        for (i in x){
            orderItems.add(
                Inventory(
                    id = i.id,
                    barcodeId = i.inventoryId.toString(),
                    name = i.itemName,
                    stockQuantity = i.quantity,
                    discountRate = i.discountRate,
                    finalPrice = i.totalPrice,
                    price  = i.unitPrice,
                    unitType = "",
                    categories = "",
                    description = "",
                    status = "",
                    createdAt = "",
                    updatedAt = ""
                )
            )
        }
        invoiceItemAdapter = invoiceItemAdapter(orderItems)
        activityBinding.ItemListView.adapter = invoiceItemAdapter
    }
}


