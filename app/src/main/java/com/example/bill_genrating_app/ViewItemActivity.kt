package com.example.bill_genrating_app

import android.os.Bundle
import android.widget.Toast
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
        val barcodeid = intent.getStringExtra("itemId")
        //this is for only test purpose

        val item = barcodeid?.let { fetchData(it.toLong()) }
        thisPageBinding.itemName.text = item?.Name.toString()
        thisPageBinding.itemMRP.text = item?.MRP.toString().plus(" Rs.")
//        thisPageBinding?.text = item?.weight.toString().plus(item?.weightType.toString())
//        thisPageBinding.itemCatagory.text = item?.Type.toString()
        thisPageBinding.Qunatity.setText(item?.stockQuantity.toString())
        thisPageBinding.DiscountedRate.text = item?.discountRate.toString().plus(" %")


        //add ten to the item
        thisPageBinding.tenPercentagecardView.setOnClickListener {
            //TODO:: update the item in database
            Toast.makeText(this, "ten", Toast.LENGTH_SHORT).show()
            val quntity = thisPageBinding.Qunatity.text.toString().toLong()
            val final = quntity!!.plus(10)
            val id = item?.BarcodeId
            if (id != null) {
                updateitemQunatity(id, final)
                thisPageBinding.Qunatity.text = final.toString()
            }
        }
        // add twenty to the item
        thisPageBinding.twentybtn.setOnClickListener {
                Toast.makeText(this, "twenty", Toast.LENGTH_SHORT).show()
                val quntity = thisPageBinding.Qunatity.text.toString().toLong()
                val final = quntity!! +20;
                val id = item?.BarcodeId
                if(id != null){
                    updateitemQunatity(id,final)
                    thisPageBinding.Qunatity.text = final.toString()
                }
            }
        //add fifty to the item
        thisPageBinding.fiftybtn.setOnClickListener {
            val quntity = thisPageBinding.Qunatity.text.toString().toLong()
            val final = quntity!! +50;
            val id = item?.BarcodeId
            if(id != null){
                updateitemQunatity(id,final)
                thisPageBinding.Qunatity.text = final.toString()
            }
        }
        //TODO:: YOU HAVE TO COMPLETE THE TASK TO CREATE THE CUSTOM QUANTITY ADD TO THE ITEM
        thisPageBinding.costomStockbtn.setOnClickListener {
            // here add the adapter to open and the room and the data in room
            // simple and attractive Ui
            // function simple and straight

        }

            // back btn press
            thisPageBinding.backBtn.setOnClickListener {
                finish()
            }

        }

    // update the item quntity
        fun updateitemQunatity(id: Long, quntity: Long) {
            val db = fetchDb()
            db.itemDao().updateItemsQuantity(quntity, id)
            db.close()
        }
    // fetch the database from the room
    private fun fetchDb(): DBHelper {
            val db = Room.databaseBuilder(
                applicationContext,
                DBHelper::class.java, "DatabaseBillGenerator"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            return db;
        }
    // fetch the data from database using barcode_id
    private fun fetchData(barcode: Long): items {
            val db = fetchDb();
            val item = db.itemDao().getByid(barcode)
            return item.get(0);
        }

}