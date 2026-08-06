package com.example.bill_genrating_app

import android.app.Application
import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bill_genrating_app.Api.ApiConfig
import com.example.bill_genrating_app.Api.payloads.LoginRequest
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.Api.service.AuthService
import com.example.bill_genrating_app.Api.service.OrderServices
import com.example.bill_genrating_app.Api.payloads.OrderItem
import com.example.bill_genrating_app.Api.payloads.OrderPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.bill_genrating_app.UtilClasses.SharePreferences

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
     private val api = Retrofit.Builder()
        .baseUrl("http://localhost:8090/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZXJjaGFudEB0ZXN0LmNvbSIsImlhdCI6MTc4NDA0ODg3MiwiZXhwIjoxNzg0MTM1MjcyfQ.UckOopuvlg4t4Yjo0Pu8JKR8Jdpm5M85sYh96UgbR8A"
        @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testAuth()= runBlocking{
        val authService = api.create(AuthService::class.java)
        val response = authService.login(
            LoginRequest(
                email = "merchant@test.com",
                password = "merchant@123"
            )
        )
        assertTrue(response.isSuccessful)
    }

    @Test
    fun testOrderCreate()= runBlocking {
        val OrderRepo = OrderRepo(api.create(OrderServices::class.java))
        val order_item = ArrayList<OrderItem>()
        order_item.add(
            OrderItem(
                inventoryId = 1,
                unitPrice = 500,
                quantity = 2,
                discountRate = 5
            )
        )

        val order = OrderPayload(
            orderItems= order_item,
            customerName = "Test",
            paymentMethod = "CASH",
            paymentStatus = "PENDING"
        )
        val response = OrderRepo.createOrder(token= token, order = order)
        println("Code : ${response.code()}")
        println("Message : ${response.message()}")
        println("Error : ${response.errorBody()?.string()}")
        println("Body : ${response.body()}")
        assertTrue(response.isSuccessful)
    }


}
