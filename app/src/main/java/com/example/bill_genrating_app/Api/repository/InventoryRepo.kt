package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.payloads.InventoryPayload
import com.example.bill_genrating_app.Api.service.InventoryService

class InventoryRepo(private val inventoryService: InventoryService) {
    suspend fun getInventoryAllItems(token: String) =
        inventoryService.getInventoryItems("Bearer $token")

    suspend fun getInventoryItemByBarcode(token: String, barcodeId: String) =
        inventoryService.getInventoryItemByBarcode("Bearer $token", barcodeId)

    suspend fun getInventoryById(token: String, id: Int) =
        inventoryService.getInventoryById("Bearer $token", id)

    suspend fun createInventoryItem(token: String, inventoryItem: InventoryPayload) =
        inventoryService.createInventoryItem("Bearer $token", inventoryItem)
}
