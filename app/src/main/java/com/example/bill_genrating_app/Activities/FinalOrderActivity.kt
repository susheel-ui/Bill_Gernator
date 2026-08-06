package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG

import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.widget.Toast

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
import com.example.bill_genrating_app.entity.Invoice_item
import com.example.bill_genrating_app.entity.invoiceItem
import com.example.bill_genrating_app.viewModels.Factories.ViewOrderViewFactory
import com.example.bill_genrating_app.viewModels.OrderState
import com.example.bill_genrating_app.viewModels.ViewOrderViewModel
import kotlin.text.clear

class FinalOrderActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityFinalOrderBinding

    var orderData: Order? = null
    val orderItems = ArrayList<Invoice_item>()
    lateinit var launcherActivity: ActivityResultLauncher<Intent>
    lateinit var invoiceItemAdapter: invoiceItemAdapter

    val viewModel: ViewOrderViewModel by viewModels {
        val orderId = intent.getStringExtra("OrderId") ?: ""
        ViewOrderViewFactory(application, orderId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        val OrderId = intent.getStringExtra("OrderId")

        if (OrderId == null) {
            Toast.makeText(this, "Order ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Log.d(TAG, "onCreate: Order : $OrderId")


        launcherActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
//                updateUIForPaidStatus()
                orderData?.let { data ->
                    lifecycleScope.launch {
                        OrderActivityServices(applicationContext).updateOrderStatus(
                            data.id.toString(),
                            status.PAID.toString()
                        )
                    }
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
                    Toast.makeText(this, "Failed to load order: ${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        activityBinding.btnNextProcess.setOnClickListener {
            orderData?.let { data ->
                when (data.paymentStatus) {
                    status.PAID.toString() -> {
                        // Handle Print Bill or next step
                    }

                    status.PENDING.toString() -> {
                        val intent = Intent(this, Payment_Options_Activity::class.java).apply {
                            putExtra("OrderId", data.id.toString())
                        }
                        launcherActivity.launch(intent)
                    }
                }
            } ?: run {
                Toast.makeText(this, "Order data not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setuserData() {
        val data = orderData ?: return
        activityBinding.customerName.text = data.clientName
        activityBinding.orderId.text = data.id.toString()
//        activityBinding.customerEmail.text = orderData.clientEmail
//        activityBinding.customerPhone.text = orderData.
        activityBinding.totalAmountValue.text = data.finalPrice.toString()
        activityBinding.tvGrandTotalSummary.text = data.finalPrice.toString()
        activityBinding.statusChip.text = data.paymentStatus
        when (data.paymentStatus) {
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
        activityBinding.tvPaymentMode.text = data.paymentMode
        activityBinding.tvDueDate.text = formatDateTime(data.createdAt)
        activityBinding.tvSubtotal.text = data.totalMoney.toString()

    }

    private fun setInvoicesItem(x: List<OrderItem>) {
        orderItems.clear()
        for (i in x) {
            orderItems.add(
                Invoice_item(
                    id = i.inventoryId,
                    barCodeId = i.inventoryId.toLong(),
                    name = i.itemName,
                    initialMRP = i.unitPrice,
                    initialQuantity = i.quantity,
                    initialDiscount = i.discountRate,
                    total = i.totalPrice
                )
            )
        }
        invoiceItemAdapter = invoiceItemAdapter(itemList = orderItems, flagEditable = false)
        activityBinding.ItemListView.adapter = invoiceItemAdapter
    }
}
