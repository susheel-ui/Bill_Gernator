package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.Repo.item_Repo
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.ViewModels.ViewItem.ViewItemViewModel
import com.example.bill_genrating_app.ViewModels.ViewItem.ViewItemViewModelFactory
import com.example.bill_genrating_app.databinding.ActivityViewItemBinding

class ViewItemActivity : AppCompatActivity() {
    lateinit var thisPageBinding: ActivityViewItemBinding;
    lateinit var viewItemViewModel: ViewItemViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisPageBinding = ActivityViewItemBinding.inflate(layoutInflater)
        setContentView(thisPageBinding.root)
        viewItemViewModel = ViewModelProvider(
            this,
            ViewItemViewModelFactory(item_Repo(DBHelper.getInstance(this)))
        )
            .get(ViewItemViewModel::class.java)

    }

    fun getViewModel(): ViewItemViewModel {
        val item_repo = item_Repo(DBHelper.getInstance(this))
        return ViewModelProvider(
            this,
            ViewItemViewModelFactory(item_repo)
        ).get(ViewItemViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        val barcodeid = intent.getStringExtra("itemId")
        //this is for only test purpose

        try {
            val item = barcodeid?.let { fetchData(it.toLong()) }
            thisPageBinding.itemName.text = item?.Name.toString()
            thisPageBinding.itemMRP.text = item?.MRP.toString().plus(" Rs.")
//        thisPageBinding?.text = item?.weight.toString().plus(item?.weightType.toString())
//        thisPageBinding.itemCatagory.text = item?.Type.toString()
            thisPageBinding.Qunatity.setText(item?.stockQuantity.toString())
            thisPageBinding.DiscountedRate.text = item?.discountRate.toString().plus(" %")
        } catch (e: Exception) {
            Log.d(TAG, "onStart: ${e.message} ")
        }


//        //add ten to the item
//        thisPageBinding.tenPercentagecardView.setOnClickListener {
//            //TODO:: update the item in database
//            Toast.makeText(this, "ten", Toast.LENGTH_SHORT).show()
//            val quntity = thisPageBinding.Qunatity.text.toString().toLong()
//            val final = quntity!!.plus(10)
//            val id = item?.BarcodeId
//            if (id != null) {
//                updateitemQunatity(id, final)
//                thisPageBinding.Qunatity.text = final.toString()
//            }
//        }
//        // add twenty to the item
//        thisPageBinding.twentybtn.setOnClickListener {
//                Toast.makeText(this, "twenty", Toast.LENGTH_SHORT).show()
//                val quntity = thisPageBinding.Qunatity.text.toString().toLong()
//                val final = quntity!! +20;
//                val id = item?.BarcodeId
//                if(id != null){
//                    updateitemQunatity(id,final)
//                    thisPageBinding.Qunatity.text = final.toString()
//                }
//            }
//        //add fifty to the item
//        thisPageBinding.fiftybtn.setOnClickListener {
//            val quntity = thisPageBinding.Qunatity.text.toString().toLong()
//            val final = quntity!! +50;
//            val id = item?.BarcodeId
//            if(id != null){
//                updateitemQunatity(id,final)
//                thisPageBinding.Qunatity.text = final.toString()
//            }
//        }
//        //TODO:: YOU HAVE TO COMPLETE THE TASK TO CREATE THE CUSTOM QUANTITY ADD TO THE ITEM
//        thisPageBinding.costomStockbtn.setOnClickListener {
//            // here add the adapter to open and the room and the data in room
//            // simple and attractive Ui
//            // function simple and straight
//
//        }

        // back btn press
        thisPageBinding.backBtn.setOnClickListener {
            finish()
        }

    }

    // update the item quntity
    fun updateitemQunatity(id: Long, quntity: Long) {
//            val db = fetchDb()
//            db.itemDao().updateItemsQuantity(quntity, id)
//            db.close()
        viewItemViewModel.updateItemsQuantity(quntity, id)
    }

    // fetch the data from database using barcode_id
    private fun fetchData(barcode: Long): items {
        //            val item = db.itemDao().getByid(barcode)
        val item = viewItemViewModel.getItemBYId(barcode)
        return item.get(0);
    }

}