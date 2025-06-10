package com.example.bill_genrating_app.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    var ActivityBinding:ActivityLoginBinding ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(ActivityBinding?.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        ActivityBinding?.btnLogin?.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_popup)
            CoroutineScope(Dispatchers.Main).launch{
                ActivityBinding?.loginbtnCard?.startAnimation(anim)
            }.invokeOnCompletion {
                startActivity(intent)
            }
        }
        return super.onCreateView(name, context, attrs)
    }
}