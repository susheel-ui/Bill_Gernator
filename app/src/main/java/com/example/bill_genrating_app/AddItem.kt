package com.example.bill_genrating_app

import android.content.Context
import android.content.pm.PackageManager
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.databinding.ActivityAddItemBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Scanner

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
        thisActivityBinding.btnback.setOnClickListener {
            finish()
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
                // and add the radio button in form of add items ui


            }

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


}