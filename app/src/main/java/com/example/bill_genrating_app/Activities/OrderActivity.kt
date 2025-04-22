package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityOrderBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Scanner

class OrderActivity : AppCompatActivity() {
    var activity:ActivityOrderBinding ?= null
    private var lastText: String? = null
    private lateinit var beepManager: BeepManager
    override fun onCreate(savedInstanceState: Bundle?) {
        activity = ActivityOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(activity!!.root)

        //barcode scanner preview
        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        activity?.barcodeScanner?.decoderFactory = DefaultDecoderFactory(formats)
        activity?.barcodeScanner?.initializeFromIntent(intent)
        activity?.barcodeScanner?.decodeContinuous(callback)
        beepManager = BeepManager(this)
    }
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            lastText = result.text
            Log.d(TAG, "barcodeResult: ${lastText.toString()}")
            activity?.barcodeScanner?.setStatusText(result.text)

            beepManager.playBeepSoundAndVibrate()

            // Preview of scanned barcode
////            val imageView = findViewById<ImageView>(R.id.barcodePreview)
//            val imageView = activity?.barcodePreview
//            imageView?.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
        }

    }
        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
    fun triggerScan(view: View) {
        activity?.barcodeScanner?.decodeSingle(callback)
    }

        override fun onStart() {
        super.onStart()

    }
    override fun onResume() {
        super.onResume()
        activity?.barcodeScanner?.resume()
    }

    override fun onPause() {
        super.onPause()
        activity?.barcodeScanner?.pause()
    }

}