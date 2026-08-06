package com.example.bill_genrating_app.Api.service

import com.example.bill_genrating_app.Api.payloads.InventoryPayload
import com.example.bill_genrating_app.Api.response.Inventory
import com.google.android.material.internal.EdgeToEdgeUtils
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface InventoryService {
    @GET("/inventory/all")
    suspend fun getInventoryItems(@Header("Authorization") token: String): Response<List<Inventory>>
    @GET("/inventory/barcode/{barcodeId}")
    suspend fun getInventoryItemByBarcode(@Header("Authorization") token: String, @Path("barcodeId") barcodeId: String): Response<Inventory>
    @POST("/inventory/create")
    suspend fun createInventoryItem(@Header("Authorization") token: String, @Body inventoryItem: InventoryPayload): Response<Inventory>
    @GET("/inventory/{id}")
    suspend fun getInventoryById(@Header("Authorization") token:String,@Path("id") id:Int): Response<Inventory>

}
