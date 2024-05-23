package com.example.bill_genrating_app

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.bill_genrating_app.Adapters.AdapterItems
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
        enableEdgeToEdge()
        thisActivityBinding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(thisActivityBinding.root)
        checkPermissionCamera(this)
        setCategory()
        thisActivityBinding.btnback.setOnClickListener {
            finish()
        }
        thisActivityBinding.btnClear.setOnClickListener {
            clearFields()
        }
        thisActivityBinding.BarcodeNo.setEndIconOnClickListener{
            Toast.makeText(this, "working", Toast.LENGTH_SHORT).show()

            val scanoption = ScanOptions()
            scanoption.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            scanoption.setPrompt("Scan the barcode")
            scanoption.setCameraId(0)
            scanoption.setBeepEnabled(true)
            scanoption.setBarcodeImageEnabled(true)
            scanoption.setOrientationLocked(false)

            barcodeLauncher.launch(scanoption)

        }

        //on click of save button data will save
            thisActivityBinding.btnSave.setOnClickListener {

                //TODO:: next day work will start from here
                // and add the radio button in form of add items and other thing happens


                try{

                    val db = Room.databaseBuilder(
                        applicationContext,
                        DBHelper::class.java,"DatabaseBillGenerator"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                    val itemDao = db.itemDao()
//                    itemDao.SaveNewItem(items(876543234567,"shampoo","250","Ml","Hair",150.00))
                    if(thisActivityBinding.barcodeFieldtext.text.toString().isNotEmpty() &&
                        thisActivityBinding.itmeName.text.isNotEmpty() &&
                        thisActivityBinding.itemMRP.text.isNotEmpty() &&
                        thisActivityBinding.itemweight.text.isNotEmpty()
                        ){
                        var barCode = thisActivityBinding.barcodeFieldtext.text.toString().toLong()
                        var name = thisActivityBinding.itmeName.text.toString()
                        var MRP = thisActivityBinding.itemMRP.text.toString().toDouble()
                        var quantity = thisActivityBinding.itemweight.text.toString()
                        var type = thisActivityBinding.categoryField.selectedItem.toString()
                        var quantityType = getQuantityType() ;




                        Log.d(TAG, "onCreate: bar code :- $barCode  Name :- $name MRP :- $MRP quantity :- $quantity qunatitytype :$quantityType  type = $type")

//
                        if(type != "Select"){
                            itemDao.SaveNewItem(items(barCode,name,quantity,quantityType,type,MRP))
                            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                            clearFields()
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
        thisActivityBinding.QuantityType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{
                group,checkedId ->
            run {
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(this, "${radio.text}", Toast.LENGTH_SHORT).show()
            }
        })

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
        val adpter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listCatagory)
        thisActivityBinding.categoryField.setAdapter(adpter)
    }





}