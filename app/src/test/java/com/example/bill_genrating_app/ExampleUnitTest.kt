package com.example.bill_genrating_app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.payloads.LoginRequest
import com.example.bill_genrating_app.Api.service.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }




//        @Test
//        fun apiTest() = runBlocking {
//            val authService = ApiConfig.retrofit
//                .create(AuthService::class.java)
//
//            val result = authService.login(
//                LoginRequest(
//                    "admin@test.com",
//                    "admin@123"
//                )
//            )
//
//            assertEquals(
//                "Login successful",
//                result.message
//            )
//        }
    }
