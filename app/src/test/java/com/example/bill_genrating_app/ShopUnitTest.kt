package com.example.bill_genrating_app

import com.example.bill_genrating_app.Api.repository.ShopRepo
import com.example.bill_genrating_app.Api.service.ShopService
import com.example.bill_genrating_app.UtilClasses.SharePreferences
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShopUnitTest {
    @Test
    fun testGetShop() = runBlocking {
        val api = Retrofit.Builder()
            .baseUrl("http://localhost:8090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val repo = ShopRepo(api.create(ShopService::class.java))
        val token =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZXJjaGFudDRAdGVzdC5jb20iLCJpYXQiOjE3ODQxMTQ2NzksImV4cCI6MTc4NDIwMTA3OX0.eDVOJqm7z24PCzP2a9p-tn6H6yRQq6pvxr0wtv5Ax18"
        val response = repo.getShopByUser(token)
        assert(response.isSuccessful)
    }
}