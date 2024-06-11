package com.example.bill_genrating_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.databinding.ActivityViewItemBinding

class ViewItemActivity : AppCompatActivity() {
    lateinit var thisPageBinding:ActivityViewItemBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        thisPageBinding = ActivityViewItemBinding.inflate(layoutInflater)
        setContentView(thisPageBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onStart() {
        super.onStart()
        val barcodeid= intent.getStringExtra("itemId")
        //this is for only test purpose

        val item = barcodeid?.let { fetchData(it.toLong()) }
        thisPageBinding.itemName.text = item?.Name.toString()
        thisPageBinding.itemMRP.text = item?.MRP.toString().plus(" Rs.")
//        thisPageBinding?.text = item?.weight.toString().plus(item?.weightType.toString())
//        thisPageBinding.itemCatagory.text = item?.Type.toString()
        thisPageBinding.Qunatity.setText(item?.stockQuantity.toString())
        thisPageBinding.DiscountedRate.text = item?.discountRate.toString()

//        thisPageBinding.btnminus.setOnClickListener {
//            var Quntity = thisPageBinding.itemQuntity.text.toString().toLong()
//            if(Quntity.toInt() != 0){
//                Quntity -= 1;
//            }
//            thisPageBinding.itemQuntity.setText(Quntity.toString())
//        }
//        thisPageBinding.btnadd.setOnClickListener {
//            var Quntity = thisPageBinding.itemQuntity.text.toString().toLong()
//            thisPageBinding.itemQuntity.setText((Quntity + 1).toString())
//        }
//        thisPageBinding.btnUpdate.setOnClickListener {
//         //TODO:: update the item in database
//            val quntity = thisPageBinding.itemQuntity.text.toString().toLong()
//            val id = item?.BarcodeId
//            if (id != null) {
//                updateitem(id,quntity)
//            }
//
//        }
//        thisPageBinding.btncancel.setOnClickListener {
//            //TODO:: cancel the item in database
//            finish()
//        }

    }
    fun updateitem(id:Long,quntity:Long){
        val db  = fetchDb()
        db.itemDao().updateItemsQuantity(quntity,id)
    }
    public fun fetchDb():DBHelper{
        val db = Room.databaseBuilder(
            applicationContext,
            DBHelper::class.java
            ,"DatabaseBillGenerator"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        return db;
    }

    public fun fetchData(barcode:Long):items{
        val db = fetchDb();
        val item = db.itemDao().getByid(barcode)
        return item.get(0);
    }
}