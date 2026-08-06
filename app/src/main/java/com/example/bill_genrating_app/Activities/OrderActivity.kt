package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bill_genrating_app.Adapters.invoiceItemAdapter
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.Repos.OrderActivityServices
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.databinding.ActivityOrderBinding
import com.example.bill_genrating_app.entity.Invoice_item
import com.example.bill_genrating_app.viewModels.CreateOrderViewModel
import com.example.bill_genrating_app.viewModels.OrderSave
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderActivity : AppCompatActivity() {
    var activity: ActivityOrderBinding? = null
    private var lastText: String? = null
    private lateinit var beepManager: BeepManager
    private var itemList = ArrayList<Invoice_item>()
    private lateinit var invoiceItemAdapter: invoiceItemAdapter
    private var grandTotal = 0.00
    private lateinit var db: DBHelper
    lateinit var orderId: String

    val OrderViewModel : CreateOrderViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        activity = ActivityOrderBinding.inflate(layoutInflater)

        // Only for Testing Purpose TODO:: remove this two line after completed code
        OrderViewModel.addItem("8901725001234")
        OrderViewModel.addItem("8901725001256")

        invoiceItemAdapter = invoiceItemAdapter(itemList, onDataChanged = { value->
            when(value.second){
                "Add" ->{
                    OrderViewModel.updateQuantity(value.first, true)
                }
                "Dec" ->{
                    OrderViewModel.updateQuantity(value.first, false)
                }
                "Del"->{
                    OrderViewModel.removeItem(value.first)
                }
            }
        })

        super.onCreate(savedInstanceState)
        setContentView(activity!!.root)
        db = DBHelper.getDatabase(applicationContext)
        //barcode scanner preview
        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        activity?.barcodeScanner?.decoderFactory = DefaultDecoderFactory(formats)
        activity?.barcodeScanner?.initializeFromIntent(intent)
        activity?.barcodeScanner?.decodeContinuous(callback)
        beepManager = BeepManager(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        activity?.invoiceRow?.layoutManager = layoutManager
        activity?.invoiceRow?.adapter = invoiceItemAdapter
        val intentOrderId = intent.getStringExtra("OrderId")
        if(intentOrderId != null){
            orderId = intentOrderId
            lifecycleScope.launch {
                val orderJob = async {
                    OrderActivityServices(applicationContext).getOrderInfoDataUsingOrderID(orderId)
                }.await()
                val itemJob = async {
                    OrderActivityServices(applicationContext).getAllItemUsingOrderId(orderId)
                }.await()
                if(orderJob != null){
//                    activity?.etName?.setText(orderJob.name)
//                    activity?.etMobile?.setText(orderJob.mob)
                }
            }
            Log.d(TAG, "onCreate:You are coming to via intent $orderId")
        }else{
           orderId = generateOrderId()
            Log.d(TAG, "onCreate: You are coming to direct")
        }

        OrderViewModel.grandTotal.observe(this){
            activity?.tvGrandTotal?.text = it.toString()
            grandTotal = it
        }

        OrderViewModel.orderApiCalling.observe(this){
            when(it){
                is OrderSave.Loading ->{
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    activity?.ordersPageSaveBtn?.isClickable = false
                }
                is OrderSave.Success ->{
                    Toast.makeText(this, "Order Saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, FinalOrderActivity::class.java).apply {
                        putExtra("OrderId",it.orderId.toString())
                    })
                    finish()
                }
                is OrderSave.Failed ->{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    activity?.ordersPageSaveBtn?.isClickable = true
                }
            }
        }

        activity?.ordersPageSaveBtn?.setOnClickListener {
            try {
                val clientName = activity?.clientName?.text.toString()
                val clientMobile = activity?.clientMobile?.text.toString()
                
                val anim = AnimationUtils.loadAnimation(this, R.anim.btn_popup)
                activity?.ordersPageSaveBtn?.startAnimation(anim)

               if(clientMobile.isNotEmpty() && clientName.isNotEmpty()){
                   Log.d(TAG, "Order Saving: $clientMobile and $clientName ")
                   OrderViewModel.setName(clientName)
                   OrderViewModel.SaveOrder()
               }else{
                   Toast.makeText(this, "Pls Enter Client Name and client Mobile Properly", Toast.LENGTH_SHORT).show()
               }
            } catch (e: Exception) {
                Log.d(TAG, "onCreate:Saving data to db ${e.message}")
            }
        }

        OrderViewModel.itemList.observe(this){
            itemList.clear()
            Log.d(TAG, "onCreate: Order Activity $it")
            itemList.addAll(it)
            invoiceItemAdapter.notifyDataSetChanged()
        }

    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                try {
                    OrderViewModel.addItem(lastText.toString())
                } catch (e: Exception) {
                    Log.d(TAG, "barcodeResult: ${e.message}")
                }

                activity?.barcodeScanner?.setStatusText(result.text)
                beepManager.playBeepSoundAndVibrate()
                activity?.barcodeScanner?.pause()
                Handler().postDelayed({
                    activity?.barcodeScanner?.resume()
                }, 1000)
                return
            }
            lastText = result.text
            Log.d(TAG, "barcodeResult: ${lastText.toString()}")
            OrderViewModel.addItem(lastText.toString())
            activity?.barcodeScanner?.setStatusText(result.text)
            beepManager.playBeepSoundAndVibrate()
            activity?.barcodeScanner?.pause()
            Handler().postDelayed({
                activity?.barcodeScanner?.resume()
            }, 1000)
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.barcodeScanner?.resume()
    }

    override fun onResume() {
        super.onResume()
        activity?.barcodeScanner?.resume()
    }

    override fun onPause() {
        super.onPause()
        activity?.barcodeScanner?.pause()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun generateOrderId(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        return "ORD" + current.format(formatter)
    }
}
