package com.example.bill_genrating_app.Api

import com.example.bill_genrating_app.Api.Util.CurlInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
//import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val client = OkHttpClient.Builder()
        .addInterceptor(CurlInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ibill-production.up.railway.app/")
            .baseUrl("http://192.168.29.160:8090/")
//            .baseUrl("http://10.55.234.84:8090/")
//            .baseUrl("http://127.0.0.1:8090/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}