package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Fragments.Fragment_Invoice_billingItems
import com.example.bill_genrating_app.Fragments.QR_CodePreview
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.Order
import com.example.bill_genrating_app.Roomdb.entities.OrderItem
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.UtilClasses.FragementsName
import com.example.bill_genrating_app.UtilClasses.change_fragment
import com.example.bill_genrating_app.UtilClasses.status
import com.example.bill_genrating_app.databinding.ActivityFinalOrderBinding
import com.example.bill_genrating_app.entity.invoiceItem
import com.google.zxing.qrcode.encoder.QRCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FinalOrderActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityFinalOrderBinding
    lateinit var db: DBHelper;
    lateinit var orderData: Order
    lateinit var invoiceItemList: ArrayList<invoiceItem>
    lateinit var lastFragmentName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val OrderId = intent.getStringExtra("OrderId")
        db = DBHelper.getDatabase(this);
        invoiceItemList = ArrayList()
//        Log.d(TAG, "onCreate:  $OrderId")   // for only testing purpose and working propper tested


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
            if (lastFragmentName != FragementsName.QRCODE.toString()) {
                val navigation: Boolean =
                    if (lastFragmentName == FragementsName.SHOWITEMS.toString()) {
                        true;
                    } else {
                        false
                    }
                change_fragment(
                    QR_CodePreview(),
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
        activityBinding.btnIconShare.setOnClickListener {
            if (lastFragmentName != FragementsName.SHARE.toString()) {
                change_fragment(
                    QR_CodePreview(),
                    activityBinding.boxView.id,
                    "QR",
                    supportFragmentManager
                )

            }
            activityBinding.btnIconShare.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.btn_popup
                )
            )
            lastFragmentName = FragementsName.SHARE.toString()
        }
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
            startActivity(Intent(this, OrderActivity::class.java))
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

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }
}