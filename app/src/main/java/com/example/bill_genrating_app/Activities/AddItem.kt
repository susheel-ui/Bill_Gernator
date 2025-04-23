package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.DBHelper
import com.example.bill_genrating_app.Roomdb.entities.items
import com.example.bill_genrating_app.databinding.ActivityAddItemBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class AddItem : AppCompatActivity() {
    lateinit var thisActivityBinding:ActivityAddItemBinding


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted :Boolean->
        if(isGranted){
            Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show()
        }else{
            //explain why you need permission
            Toast.makeText(this, "you need to give permission to access Camera", Toast.LENGTH_SHORT).show()
        }
    }



    val barcodeLauncher = registerForActivityResult(ScanContract()){
        result:ScanIntentResult ->
        run {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                setResult(result.contents)
            }
        }
    }
    fun  setResult(str:String){
        Toast.makeText(this, "--> $str", Toast.LENGTH_SHORT).show()
        thisActivityBinding.barcodeFieldtext.setText(str)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivityBinding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(thisActivityBinding.root)
        checkPermissionCamera(this)
        setCategory()
        launchScanner()
        thisActivityBinding.btnback.setOnClickListener {
            finish()
        }
        thisActivityBinding.btnClear.setOnClickListener {
            clearFields()
        }
        thisActivityBinding.BarcodeNo.setEndIconOnClickListener{
            Toast.makeText(this, "working", Toast.LENGTH_SHORT).show()
            launchScanner()
        }

        //on click of save button data will save
            thisActivityBinding.btnSave.setOnClickListener {

                //TODO:: next day work will start from here

                onbtnSaveClick()
            }

        // listener for get Quantity type
        thisActivityBinding.QuantityType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
                group,checkedId ->
            run {
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(this, "${radio.text}", Toast.LENGTH_SHORT).show()
            }
        })



    }
    private fun launchScanner(){
        val scanoption = ScanOptions()
        scanoption.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        scanoption.setPrompt("Scan the barcode")
        scanoption.setCameraId(0)
        scanoption.setBeepEnabled(true)
        scanoption.setBarcodeImageEnabled(true)
        scanoption.setOrientationLocked(false)
        barcodeLauncher.launch(scanoption)
    }

    private fun onbtnSaveClick(){
        try{

            val db = Room.databaseBuilder(
                applicationContext,
                DBHelper::class.java,"DatabaseBillGenerator"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
            val itemDao = db.itemDao()
//                    itemDao.SaveNewItem(items(876543234567,"shampoo","250","Ml","Hair",150.00))
            if(thisActivityBinding.barcodeFieldtext.text.toString().isNotEmpty() &&
                thisActivityBinding.itmeName.text.isNotEmpty() &&
                thisActivityBinding.itemMRP.text.isNotEmpty() &&
                thisActivityBinding.itemweight.text.isNotEmpty() &&
                thisActivityBinding.stockQuantity.text.isNotEmpty() &&
                thisActivityBinding.discountRate.text.isNotEmpty()
            ){
                val barCode = thisActivityBinding.barcodeFieldtext.text.toString().toLong()
                val name = thisActivityBinding.itmeName.text.toString()
                val MRP = thisActivityBinding.itemMRP.text.toString().toDouble()
                val quantity = thisActivityBinding.itemweight.text.toString()
                val type = thisActivityBinding.categoryField.selectedItem.toString()
                val quantityType = getQuantityType();
                val stock = thisActivityBinding.stockQuantity.text.toString().toLong()
                val discountRate = thisActivityBinding.discountRate.text.toString().toDouble()




//                Log.d(TAG, "onCreate: bar code :- $barCode  Name :- $name MRP :- $MRP quantity :- $quantity qunatitytype :$quantityType  type = $type")

//
                if(type != "Select"){
                    itemDao.SaveNewItem(items(barCode,name,quantity,quantityType,type,MRP,stock,discountRate))
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    clearFields()
                    finish()
                }
                else{
                    Toast.makeText(this, "pls select type", Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(this, "pls enter the fields properly", Toast.LENGTH_LONG).show()
            }
            //at this commit the db is working properly

        }catch(Exception:Exception){
            Log.d(TAG, "onCreate Error: ${Exception.message}")
            Toast.makeText(this, "product is already exist", Toast.LENGTH_SHORT).show()
        }

    }
    private fun clearFields(){
        thisActivityBinding.barcodeFieldtext.setText("")
        thisActivityBinding.itemMRP.setText("")
        thisActivityBinding.itemweight.setText("")
        thisActivityBinding.itmeName.setText("")
    }
    private fun checkPermissionCamera(context: Context){
                if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

                }
        else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                    Toast.makeText(this, "camera permission requered", Toast.LENGTH_SHORT).show()
                }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
    }

    private fun getQuantityType():String{
        val selector = thisActivityBinding.QuantityType.checkedRadioButtonId;
        val radioSelected = findViewById<RadioButton>(selector)
        return  radioSelected.text.toString()
    }


    private fun setCategory(){
        val listCatagory = resources.getStringArray(R.array.Catogory)
        val adpter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,listCatagory)
        thisActivityBinding.categoryField.setAdapter(adpter)
    }





}