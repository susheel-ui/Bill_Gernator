package com.example.bill_genrating_app.Fragments

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.room.util.newStringBuilder
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.FragmentQRCodePreviewBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QR_CodePreview.newInstance] factory method to
 * create an instance of this fragment.
 */
class QR_CodePreview(val data:JSONObject) : Fragment() {
    private lateinit var fragementBinding:FragmentQRCodePreviewBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragementBinding = FragmentQRCodePreviewBinding.inflate(layoutInflater)
        val qrCode = generateQRCode(data)
        fragementBinding.barCodeImageView.setImageBitmap(qrCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return fragementBinding.root
    }

    fun generateQRCode(content: JSONObject, size: Int = 500): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(content.toString(), BarcodeFormat.QR_CODE, size, size)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}