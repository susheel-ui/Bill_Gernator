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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bill_genrating_app.R
import com.example.bill_genrating_app.Roomdb.Repos.UserService
import com.example.bill_genrating_app.databinding.ActivityloginBinding
import com.example.bill_genrating_app.viewModels.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.bill_genrating_app.UtilClasses.UtilString
import com.example.bill_genrating_app.viewModels.LoginState

class LoginActivity : AppCompatActivity() {
    var ActivityBinding: ActivityloginBinding? = null
    val viewModel: AuthViewModel by viewModels()
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityBinding = ActivityloginBinding.inflate(layoutInflater)
        setContentView(ActivityBinding?.root)
        sharedPref = applicationContext.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val status = sharedPref.getBoolean("isLoggedIn", false)
        val sharedUserid = sharedPref.getInt("userId", -1)
//        if (status) {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("_id", sharedUserid)
//            startActivity(intent)
//        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        ActivityBinding?.btnLogin?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_popup)
//            CoroutineScope(Dispatchers.Main).launch{
////                ActivityBinding?.loginbtnCard?.startAnimation(anim)
//            }.invokeOnCompletion {
            val email = ActivityBinding?.emailEt?.text.toString()
            val password = ActivityBinding?.password?.text.toString()

//                    val user = UserService(applicationContext).authenticateUser(email,password)
            viewModel.login(email, password)
//                Log.d(TAG, "onCreateView: ${user?.id}")
//                    if(user != null) {
//                        intent.putExtra("_id", user.id)
//
//                        sharedPref.edit {
//                            putBoolean("isLoggedIn", true)
//                            putInt("userId", user.id!!)
//
//                        }
//
//                        startActivity(intent)
//                    }else{
//                        Toast.makeText(this, "Wrong id or password", Toast.LENGTH_SHORT).show()
//                    }


//            }
        }

        viewModel.loginStateLiveData.observe(this) {
            when (it) {
                is LoginState.Error -> {
                    if(it.code == 403){
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                    ActivityBinding?.btnLogin?.text = "Login"
                    ActivityBinding?.btnLogin?.isEnabled = true
                }

                is LoginState.Loading -> {
//                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    ActivityBinding?.btnLogin?.text = "..."
                    ActivityBinding?.btnLogin?.isEnabled = false
                }

                is LoginState.Success -> {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onCreateView: success ${it.message}")
                    if(it.code == 200){
                        sharedPref.edit {
                            putBoolean(UtilString.isLoggedIn.toString(), true)
                            putString(UtilString.Token.toString(), it.message?.token)
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        }
        ActivityBinding?.createanaccountbtn?.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
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