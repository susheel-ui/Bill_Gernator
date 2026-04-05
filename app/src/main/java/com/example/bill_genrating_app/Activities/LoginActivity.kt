package com.example.bill_genrating_app.Activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.Repos.UserService
import com.example.bill_genrating_app.databinding.ActivityloginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    var ActivityBinding: ActivityloginBinding ?= null
    lateinit var sharedPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityBinding = ActivityloginBinding.inflate(layoutInflater)
        setContentView(ActivityBinding?.root)
        sharedPref =   applicationContext.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val status = sharedPref.getBoolean("isLoggedIn",false)
        val sharedUserid = sharedPref.getInt("userId",-1)
        if (status){
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("_id", sharedUserid)
            startActivity(intent)
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        ActivityBinding?.btnLogin?.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_popup)
            CoroutineScope(Dispatchers.Main).launch{
//                ActivityBinding?.loginbtnCard?.startAnimation(anim)
            }.invokeOnCompletion {
                val email = ActivityBinding?.emailEt?.text.toString()
                val password = ActivityBinding?.password?.text.toString()

                    val user = UserService(applicationContext).authenticateUser(email,password)
//                Log.d(TAG, "onCreateView: ${user?.id}")
                    if(user != null) {
                        intent.putExtra("_id", user.id)

                        val editor = sharedPref.edit()
                        editor.putBoolean("isLoggedIn", true)
             editor.putInt("userId", user.id!!)

                        editor.apply()

                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Wrong id or password", Toast.LENGTH_SHORT).show()
                    }

            }
        }
        ActivityBinding?.createanaccountbtn?.setOnClickListener{
            val intent = Intent(this,RegisterUserActivity::class.java)
            startActivity(intent)
        }
        return super.onCreateView(name, context, attrs)
    }
    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }
    fun isUserLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("isLoggedIn", false)
    }
}