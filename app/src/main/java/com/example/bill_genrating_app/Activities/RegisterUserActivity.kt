package com.example.bill_genrating_app.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bill_genrating_app.Roomdb.Repos.UserService
import com.example.bill_genrating_app.Roomdb.entities.User
import com.example.bill_genrating_app.databinding.ActivityRegisterUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterUserActivity : AppCompatActivity() {
    lateinit var activityBinding : ActivityRegisterUserBinding
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        activityBinding.btnSignUp.setOnClickListener {
            val username = activityBinding.usernameET.text.toString()
            val password = activityBinding.passwordET.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val id = withContext(Dispatchers.IO) {
                            UserService(applicationContext).registerUser(User(null, username, password))
                        }

                        if (id != null) {
                            val intent = Intent(this@RegisterUserActivity, ShopDetailsEditPage::class.java)
                            intent.putExtra("_id", id)
                            resultLauncher.launch(intent)
                        } else {
                            Toast.makeText(this@RegisterUserActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@RegisterUserActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }

        }

    }
}