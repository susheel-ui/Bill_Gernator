package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.service.InventoryService

class InventoryRepo(private val inventoryService: InventoryService) {
        suspend fun getInventoryItems(token:String) = inventoryService.getInventoryItems("Bearer $token")
}
