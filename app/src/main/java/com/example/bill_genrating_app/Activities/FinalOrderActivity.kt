package com.example.bill_genrating_app.Activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bill_genrating_app.Adapters.invoiceItemAdapter
import com.example.bill_genrating_app.Fragments.Fragment_Invoice_billingItems
import com.example.bill_genrating_app.Fragments.QR_CodePreview
import com.example.bill_genrating_app.Payment_Options_Activity
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.Repos.OrderActivityServices
import com.example.bill_genrating_app.Roomdb.Repos.shopDetailsService
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.Roomdb.entities.shopDetails
import com.example.bill_genrating_app.UtilClasses.FragementsName
import com.example.bill_genrating_app.UtilClasses.change_fragment
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding
import com.example.bill_genrating_app.databinding.PrintPdfInvoiceBinding
import com.example.bill_genrating_app.entity.invoiceItem
import com.gkemon.XMLtoPDF.PdfGenerator
import com.gkemon.XMLtoPDF.PdfGenerator.XmlToPDFLifecycleObserver
import com.gkemon.XMLtoPDF.PdfGeneratorListener
import com.gkemon.XMLtoPDF.model.FailureResponse
import com.gkemon.XMLtoPDF.model.SuccessResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinalOrderActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityFinalOrderBinding
    lateinit var db: DBHelper
    lateinit var orderData: Order
    lateinit var invoiceItemList: ArrayList<invoiceItem>
    lateinit var lastFragmentName: String
    lateinit var launcherActivity: ActivityResultLauncher<Intent>
    private lateinit var xmlToPDFLifecycleObserver: XmlToPDFLifecycleObserver
    private var id = -1
    lateinit var shopDetails: shopDetails
    lateinit var sharedPref: SharedPreferences

    @SuppressLint("ResourceType", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        val OrderId = intent.getStringExtra("OrderId")
        db = DBHelper.getDatabase(this)
        sharedPref = applicationContext.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        xmlToPDFLifecycleObserver = XmlToPDFLifecycleObserver(this)
        lifecycle.addObserver(xmlToPDFLifecycleObserver)
        invoiceItemList = ArrayList()
        
        id = sharedPref.getInt("userId", -1)
        if (id == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }

        launcherActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                updateUIForPaidStatus()
                lifecycleScope.launch {
                    OrderActivityServices(applicationContext).updateOrderStatus(
                        orderData.ordId,
                        status.PAID.toString()
                    )
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val details = shopDetailsService(applicationContext).getShopDetails(id)
            if (details != null) {
                shopDetails = details
            }
        }

        lifecycleScope.launch {
            val OrderDataJob = async { getData(OrderId.toString()) }
            val data = OrderDataJob.await()
            orderData = data

            val orderItemsJob = async { getOrderItemsList(data.ordId) }
            val orderItems = orderItemsJob.await()

            invoiceItemList.clear()
            for (x in orderItems) {
                val itemJob = async { getDetailsOfItems(x.BarcodeId) }
                val itemDetails = itemJob.await()
                if (itemDetails.isNotEmpty()) {
                    val item = itemDetails[0]
                    invoiceItemList.add(
                        invoiceItem(
                            item.BarcodeId,
                            item.Name,
                            item.MRP,
                            x.quantity,
                            item.discountRate
                        )
                    )
                }
            }

            // Populate UI after data is loaded
            populateOrderUI()
        }

        setupClickListeners(OrderId)
    }

    private fun populateOrderUI() {
        activityBinding.customerName.text = orderData.name
        activityBinding.customerPhone.text = "+91-${orderData.mob}"
        activityBinding.orderId.text = "ORD-${orderData.ordId.takeLast(6)}"
        activityBinding.totalAmountValue.text = "\u20B9${orderData.grandTotal}"
        activityBinding.tvSubtotal.text = "\u20B9${orderData.grandTotal}"
        activityBinding.tvGrandTotalSummary.text = "\u20B9${orderData.grandTotal}"
        activityBinding.statusPill.text = orderData.status
        
        val savedAmount = calculateSavedMoney(orderData.grandTotal)
        activityBinding.tvSavedAmount.text = "\u20B9${DecimalFormat("#.##").format(savedAmount)} on this order"

        setOrderItems(invoiceItemList)
        lastFragmentName = FragementsName.SHOWITEMS.toString()

        if (orderData.status == status.PENDING.toString()) {
            activityBinding.cardPrintBillbtn.text = "Make Payment"
            activityBinding.statusPill.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
            activityBinding.statusPill.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            updateUIForPaidStatus()
        }
    }

    private fun updateUIForPaidStatus() {
        activityBinding.cardPrintBillbtn.text = "Print Bill"
        activityBinding.statusPill.text = "PAID"
        activityBinding.statusPill.backgroundTintList = ContextCompat.getColorStateList(this, R.color.tertiary_container)
        activityBinding.statusPill.setTextColor(ContextCompat.getColor(this, R.color.tertiary))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupClickListeners(OrderId: String?) {
        activityBinding.finalPageBackBtn.setOnClickListener { finish() }

        activityBinding.btnIconQR.setOnClickListener {
            val data = JSONObject()
            data.put("OrderId", OrderId)
            data.put("Amount", orderData.grandTotal.toString())
            data.put("PaymentStatus", orderData.status)
            
            if (lastFragmentName != FragementsName.QRCODE.toString()) {
                change_fragment(
                    QR_CodePreview(data),
                    activityBinding.boxView.id,
                    FragementsName.QRCODE.toString(),
                    supportFragmentManager,
                    lastFragmentName == FragementsName.SHOWITEMS.toString()
                )
            }
            activityBinding.btnIconQR.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_popup))
            lastFragmentName = FragementsName.QRCODE.toString()
        }

        activityBinding.showItemIconItems.setOnClickListener {
            if (lastFragmentName != FragementsName.SHOWITEMS.toString()) {
                change_fragment(
                    Fragment_Invoice_billingItems(invoiceItemList, orderData.grandTotal.toString()),
                    activityBinding.boxView.id,
                    FragementsName.SHOWITEMS.toString(),
                    supportFragmentManager,
                    false
                )
            }
            activityBinding.showItemIconItems.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_popup))
            lastFragmentName = FragementsName.SHOWITEMS.toString()
        }

        activityBinding.cardPrintBillbtn.setOnClickListener {
            if (activityBinding.cardPrintBillbtn.text == "Make Payment") {
                val intent = Intent(this, Payment_Options_Activity::class.java)
                intent.putExtra("OrderId", orderData.ordId)
                launcherActivity.launch(intent)
            } else {
                generatePdfToUri()
            }
            activityBinding.cardPrintBillbtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_popup))
        }
        
        activityBinding.btnIconShare.setOnClickListener {
            activityBinding.btnIconShare.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_popup))
            // TODO: Implement share functionality (e.g. sharing PDF or text)
            Toast.makeText(this, "Sharing functionality coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun getData(ordId: String): Order {
        return db.orderDao().findById(ordId)!!
    }

    private suspend fun getOrderItemsList(OrderId: String): List<OrderItem> {
        return db.orderItemDao().findByOrderId(OrderId)
    }

    private fun setOrderItems(list: ArrayList<invoiceItem>) {
        Log.d(TAG, "setOrderItems: $list")
        val fragment = Fragment_Invoice_billingItems(list, orderData.grandTotal.toString())
        supportFragmentManager.beginTransaction().replace(R.id.boxView, fragment).commit()
    }

    private suspend fun getDetailsOfItems(itemId: String): List<items> {
        return db.itemDao().getByid(itemId.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun generatePdfToUri() {
        val printBinding = PrintPdfInvoiceBinding.inflate(layoutInflater)
        printBinding.subTotal.text = "Sub Total :  \u20B9${orderData.grandTotal}"
        printBinding.grandTotal.text = "Grand Total : \u20B9${orderData.grandTotal}"
        printBinding.ShopName.text = shopDetails.shopName
        printBinding.Address.text = shopDetails.address
        
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        printBinding.DateAndTime.text = current.format(formatter)

        printBinding.itemList.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = invoiceItemAdapter(invoiceItemList, true)
        printBinding.itemList.adapter = adapter
        
        val df = DecimalFormat("#,###.00")
        printBinding.discount.text = "Saved : \u20B9${df.format(calculateSavedMoney(orderData.grandTotal))}"

        PdfGenerator.getBuilder()
            .setContext(this)
            .fromViewSource()
            .fromView(printBinding.root)
            .setFileName("Invoice_${orderData.ordId}")
            .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.NONE)
            .savePDFSharedStorage(xmlToPDFLifecycleObserver)
            .build(object : PdfGeneratorListener() {
                override fun onSuccess(response: SuccessResponse) {
                    Toast.makeText(applicationContext, "Saved to selected location!", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(response: FailureResponse) {
                    Toast.makeText(applicationContext, "PDF generation failed", Toast.LENGTH_LONG).show()
                }
                override fun showLog(log: String) { Log.d("PDFGen", log) }
                override fun onStartPDFGeneration() {
                    TODO("Not yet implemented")
                }

                override fun onFinishPDFGeneration() {
                    TODO("Not yet implemented")
                }
            })
    }

    fun calculateSavedMoney(grandTotal: Double): Double {
        var mrpTotal = 0.0
        for (x in invoiceItemList) {
            mrpTotal += x.MRP * x.quantity
        }
        return mrpTotal - grandTotal
    }
}