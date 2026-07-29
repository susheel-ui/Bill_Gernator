package com.example.bill_genrating_app.Api.repository

import com.example.bill_genrating_app.Api.service.PreferencesServices

class PreferencesRepo(val preferencesServices: PreferencesServices) {
    suspend fun getMetrices(token: String) =
        preferencesServices.getMetrices("Bearer $token")
}