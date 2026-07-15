package com.example.bill_genrating_app

import com.example.bill_genrating_app.Api.payloads.InventoryPayload
import com.example.bill_genrating_app.Api.repository.InventoryRepo
import com.example.bill_genrating_app.Api.service.InventoryService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Inventorytests {

    @Test
    fun testCreateInventory() = runBlocking {
        val api = Retrofit.Builder()
            .baseUrl("http://localhost:8090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val inventoryRepo = InventoryRepo(api.create(InventoryService::class.java))
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZXJjaGFudDRAdGVzdC5jb20iLCJpYXQiOjE3ODQwOTUwNTEsImV4cCI6MTc4NDE4MTQ1MX0.c7ba3FiCDZja4kc2s-qPdYfT_YZxW4tDml09e5atuug"
            val inventoryItem = InventoryPayload(
            name = "VimBar test",
            barcodeId = "8901725001252",
            price = 45.00,
            discountRate = 12.00,
            stockQuantity = 100,
            unitType = "Ml",
            categories = "Kitchen,cleaning",
            description = "Product to clean kitchen dish",
            status = "ACTIVE"
        )
        val response = inventoryRepo.createInventoryItem(token, inventoryItem)
        println("Code : ${response.code()}")
        println("Message : ${response.message()}")
        println("Error : ${response.errorBody()?.string()}")
        println("Body : ${response.body()}")
        assert(response.isSuccessful)
    }

}