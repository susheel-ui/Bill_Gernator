package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
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
import com.example.bill_genrating_app.entity.invoiceItem
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrderActivity : AppCompatActivity() {
    var activity: ActivityOrderBinding? = null
    private var lastText: String? = null
    private lateinit var beepManager: BeepManager
    private var itemList = ArrayList<invoiceItem>()
    private lateinit var invoiceItemAdapter: invoiceItemAdapter
    private var grandTotal = 0.00;
    private lateinit var db: DBHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        activity = ActivityOrderBinding.inflate(layoutInflater)
        invoiceItemAdapter = invoiceItemAdapter(itemList)
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
        val orderId = generateOrderId()

        activity?.ordersPageSaveBtn?.setOnClickListener {
            try {
                // creating Order Entity
                val name = activity?.etName?.text.toString()
                val mob = activity?.etMobile?.text.toString()
                val order = Order(orderId, name, mob, grandTotal, status.PENDING.toString())
                val list = ArrayList<OrderItem>()
                val anim = AnimationUtils.loadAnimation(this, R.anim.btn_popup)
                activity?.ordersPageSaveBtn?.startAnimation(anim)
                itemList.forEach { it ->
                    list.add(OrderItem(order.ordId, it.barCodeId.toString(), it.quantity, it.total))
                }
                if (name.isNotEmpty()) {
                    if (mob.isNotEmpty()) {
                        if (itemList.isNotEmpty()) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                try {
                                    saveToDB(order, list)
                                } catch (e: Exception) {
                                    Log.d(TAG, "onCreate: saving error ${e.message}")
                                }
                            }
                        } else
                            Toast.makeText(this, "pls add Some Items", Toast.LENGTH_SHORT).show()

                    } else
                        Toast.makeText(this, "pls Enter Mobile number", Toast.LENGTH_LONG).show()


                } else
                    Toast.makeText(this, "pls Enter Name", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Log.d(TAG, "onCreate:Saving data to db ${e.message}")
            }


        }
        activity?.ordersPageResetBtn?.setOnClickListener {
            itemList.clear()
            invoiceItemAdapter.notifyDataSetChanged()
            activity?.etName?.text?.clear()
            activity?.etMobile?.text?.clear()
            activity?.tvGrandTotal?.text = "Grand Total: 0.00"
        }

    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                try {
                    addItemToInvoice(lastText!!.toLong())
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
            addItemToInvoice(lastText!!.toLong())
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

    fun addItemToInvoice(barcodeId: Long): Boolean {
        val itemDao = db.itemDao()
        val item = itemDao.getByid(barcodeId)
        if (item.isNotEmpty() && item.size == 1) {
            val newItem = item[0]
            val total = newItem.MRP - newItem.MRP * (newItem.discountRate / 100)
            val invoiceItemToAdd = invoiceItem(
                newItem.BarcodeId,
                newItem.Name,
                newItem.MRP,
                1,
                newItem.discountRate
            )

            var isPresent = false
            itemList.forEachIndexed { index, invoiceItem ->
                if (invoiceItem.barCodeId == invoiceItemToAdd.barCodeId) {
                    isPresent = true
                    itemList.get(index).quantity = invoiceItem.quantity + 1
                    itemList.get(index).total = invoiceItem.total + total
                    invoiceItemAdapter.notifyDataSetChanged()
                    for (x in itemList) {
                        Log.d(TAG, "addItemToInvoice: ${x.name} ${x.quantity}")
                    }
                    // You might want to update the quantity here if it's already present
                }
            }

            if (!isPresent) {
                itemList.add(invoiceItemToAdd)
                invoiceItemAdapter.notifyDataSetChanged()
                for (x in itemList) {
                    Log.d(TAG, "addItemToInvoice: ${x.name} ${x.quantity}")
                }

            }
        }
        findGrandTotal()
        return true
    }

    private fun saveToDB(order: Order, orderItems: List<OrderItem>) {
        lifecycleScope.launch {
            OrderActivityServices(applicationContext).saveToDb(order, orderItems)
        }
    }

    private fun findGrandTotal() {
        try {
            grandTotal = 0.00
            itemList.forEach { itemList ->
                grandTotal += itemList.total
            }
            val df = DecimalFormat("#,###." + "0".repeat(2))
            activity?.tvGrandTotal?.text = "Grand Total: ".plus(df.format(grandTotal))
        } catch (e: Exception) {
            Log.d(TAG, "findGrandTotal: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public fun generateOrderId(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        return "ORD" + current.format(formatter)
    }


}

