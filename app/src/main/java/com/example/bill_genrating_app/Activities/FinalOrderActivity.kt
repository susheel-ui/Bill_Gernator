package com.example.bill_genrating_app.Activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.FileInputStream
import java.io.IOException
import java.text.DecimalFormat
import kotlin.coroutines.coroutineContext


class FinalOrderActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityFinalOrderBinding
    lateinit var db: DBHelper
    lateinit var orderData: Order
    lateinit var invoiceItemList: ArrayList<invoiceItem>
    lateinit var lastFragmentName: String
    lateinit var launcherActivity: ActivityResultLauncher<Intent>
    private lateinit var xmlToPDFLifecycleObserver: XmlToPDFLifecycleObserver
//    private val sharedPrefarence = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//    lateinit var shopDetails: shopDetails

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val OrderId = intent.getStringExtra("OrderId")
        db = DBHelper.getDatabase(this);
        xmlToPDFLifecycleObserver = XmlToPDFLifecycleObserver(this)
        lifecycle.addObserver(xmlToPDFLifecycleObserver)
        invoiceItemList = ArrayList()
//        Log.d(TAG, "onCreate:  $OrderId")   // for only testing purpose and working propper tested
//        var userId = sharedPrefarence.getString("id",null)
//        if(userId != null){
//            lifecycleScope.launch {
//                shopDetails = CoroutineScope(Dispatchers.IO).async {
//                    shopDetailsService(applicationContext).getShopDetails(userId.toInt())!!
//                }.await()
//
//            }
//        }
        launcherActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                activityBinding.btnNextActivity.text = "Print Bill"
                activityBinding.cardPrintBillbtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorSuccess
                    )

                )
                lifecycleScope.launch {
                    OrderActivityServices(applicationContext).updateOrderStatus(
                        orderData.ordId,
                        status.PAID.toString()
                    )
                }
            }
        }

        lifecycleScope.launch {
            val OrderData = async {
                getData(OrderId.toString())
            }
            val data = OrderData.await() as Order

            val job2 = async {
                orderData = data
                getOrderItemsList(data.ordId)
            }
            val result = job2.await()

            invoiceItemList.clear()
            for (x in result) {
                val itemJob = async {
                    getDetailsOfItems(x.BarcodeId)
                }
                val result = itemJob.await()
                if (result.isNotEmpty()) {
                    val item = result[0];
                    invoiceItemList.add(
                        invoiceItem(
                            item.BarcodeId,
                            item.Name,
                            item.MRP,
                            x.quantity,
                            item.discountRate
                        )
                    )
                } else {
                    Log.d(TAG, "onCreate: ItemNot Found")
                }

            }

        }.invokeOnCompletion {
            activityBinding.usernameText.text = orderData.name + "( +91-${orderData.mob} )"
            activityBinding.balanceText.text = "\u20B9" + orderData.grandTotal.toString()
            setOrderItems(invoiceItemList)
            lastFragmentName = FragementsName.SHOWITEMS.toString()
            if (orderData.status == status.PENDING.toString()) {
                activityBinding.btnNextActivity.text = "Make Paymemt"
                activityBinding.cardPrintBillbtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red
                    )
                )
            } else {
                activityBinding.btnNextActivity.text = "Print Bill"
                activityBinding.cardPrintBillbtn.setCardBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorSuccess
                    )
                )
            }
        }
        activityBinding.btnIconQR.setOnClickListener {
            val data = JSONObject()
            data.append("OrderId",OrderId)
            data.append("Amount",orderData.grandTotal.toString())
            data.append("PaymentStatus",orderData.status.toString())
            if (lastFragmentName != FragementsName.QRCODE.toString()) {
                val navigation: Boolean =
                    if (lastFragmentName == FragementsName.SHOWITEMS.toString()) {
                        true;
                    } else {
                        false
                    }
                change_fragment(
                    QR_CodePreview(data),
                    activityBinding.boxView.id,
                    FragementsName.QRCODE.toString(),
                    supportFragmentManager,
                    navigation
                )
            }
            activityBinding.btnIconQR.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.btn_popup
                    )
                    )
            lastFragmentName = FragementsName.QRCODE.toString()

        }
//        activityBinding.btnIconShare.setOnClickListener {
//
//
//            if (lastFragmentName != FragementsName.SHARE.toString()) {
//                change_fragment(
//                    QR_CodePreview(),
//                    activityBinding.boxView.id,
//                    "QR",
//                    supportFragmentManager
//                )
//
//            }
//            activityBinding.btnIconShare.startAnimation(
//                AnimationUtils.loadAnimation(
//                    this,
//                    R.anim.btn_popup
//                )
//            )
//            lastFragmentName = FragementsName.SHARE.toString()
//        }
        activityBinding.showItemIconItems.setOnClickListener {
            if (lastFragmentName != FragementsName.SHOWITEMS.toString()) {
                change_fragment(
                    Fragment_Invoice_billingItems(
                        invoiceItemList,
                        orderData.grandTotal.toString()
                    ), activityBinding.boxView.id, "QR", supportFragmentManager, false
                )

            }
            activityBinding.showItemIconItems.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.btn_popup
                )
            )
            lastFragmentName = FragementsName.SHOWITEMS.toString()
        }
        activityBinding.cardPrintBillbtn.setOnClickListener {
            if(activityBinding?.btnNextActivity?.text == "Make Paymemt"){
                val intent = Intent(this, Payment_Options_Activity::class.java)
                intent.putExtra("OrderId",orderData.ordId)
                launcherActivity.launch(intent)
            }else{
                //TODO:: here we will work to activity for Printing pdf or save invoice
//                val thisIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    type = "application/pdf"
//                } // The result of this intent will be passed to the get() method of the contract.
//                createDocLauncher.launch(thisIntent)
                generatePdfToUri()
           
            }

            activityBinding.cardPrintBillbtn.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.btn_popup
                )
            )

        }
        activityBinding.btnMakeChanges.setOnClickListener {
            activityBinding.btnMakeChanges.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.btn_popup
                )
            )
            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("OrderId",orderData.ordId)
            startActivity(intent)
        }

        activityBinding.finalPageBackBtn.setOnClickListener {
            finish()
        }


    }

    private suspend fun getData(ordId: String): Order {
        return db.orderDao().findById(ordId)!!
    }

    private suspend fun getOrderItemsList(OrderId: String): List<OrderItem> {
        return db.orderItemDao().findByOrderId(OrderId)
    }

    private fun setOrderItems(list: ArrayList<invoiceItem>) {
        val fragment = Fragment_Invoice_billingItems(list, orderData.grandTotal.toString())
        supportFragmentManager.beginTransaction().replace(R.id.boxView, fragment).commit()
    }

    private suspend fun getDetailsOfItems(itemId: String): List<items> {
        return db.itemDao().getByid(itemId.toLong())
    }
    private fun getShopDetails(){

    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun generatePdfToUri() {
        val printBinding = PrintPdfInvoiceBinding.inflate(layoutInflater)
        printBinding.subTotal.text = "Sub Total :  ".plus(orderData.grandTotal)
        printBinding.grandTotal.text = "GrandTotal :   ".plus(orderData.grandTotal)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        printBinding.itemList.layoutManager = layoutManager
//
//            printBinding.ShopName.text = shopDetails.shopName
//            printBinding.Address.text = shopDetails.address

        val adapter = invoiceItemAdapter(invoiceItemList,true)
        printBinding.itemList.adapter = adapter
        val df = DecimalFormat("#,###." + "0".repeat(2))
        printBinding.discount.text = "Saved : ".plus(df.format(calculateSavedMoney(orderData.grandTotal)))
        PdfGenerator.getBuilder()
            .setContext(this)
            .fromViewSource()
            .fromView(printBinding.root)// your XML layout
            .setFileName(orderData.ordId) // filename inside scoped storage
            .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.NONE)
            .savePDFSharedStorage(xmlToPDFLifecycleObserver) // for SDK >= 33
            .build(object : PdfGeneratorListener() {
                override fun onStartPDFGeneration() {
                    // Optional: show a progress bar
                }

                override fun onFinishPDFGeneration() {

                }

                override fun onSuccess(response: SuccessResponse) {
                    try {

                        Toast.makeText(applicationContext, "Saved to selected location!", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        Toast.makeText(applicationContext, "Failed to save PDF: ${e.message}", Toast.LENGTH_LONG).show()
                        Log.e("PDFGen", "Error copying PDF: $e")
                    }
                }

                override fun onFailure(response: FailureResponse) {
                    Toast.makeText(applicationContext, "PDF generation failed: ${response.errorMessage}", Toast.LENGTH_LONG).show()
                    Log.e("PDFGen", "Generation failed: ${response.errorMessage}")
                }

                override fun showLog(log: String) {
                    Log.d("PDFGen", log)
                }
            })
    }
    fun calculateSavedMoney(grandTotal: Double): Double {
        var MRP_GrandTotal: Double = 0.0;
        for (x in invoiceItemList) {
            MRP_GrandTotal += x.MRP * x.quantity;
        }
        return MRP_GrandTotal-grandTotal;
    }

}