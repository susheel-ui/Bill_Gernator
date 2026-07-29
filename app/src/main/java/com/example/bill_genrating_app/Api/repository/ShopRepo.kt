package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.service.ShopService

class ShopRepo(val shopService: ShopService) {
    suspend fun getShopByUser(token: String) = shopService.getShopByUser("Bearer $token")
}