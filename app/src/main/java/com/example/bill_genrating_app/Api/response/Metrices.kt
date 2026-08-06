package com.example.bill_genrating_app.Api.response

data class Metrices(
    val incomeIncrementPercentage: Double,
    val lowStocksCount: Int,
    val message: String,
    val pendingOrdersCount: Int,
    val success: Boolean,
    val todayTotalIncome: Double,
    val totalInventoriesCount: Int,
    val totalInvoicesCount: Int,
    val yesterdayTotalIncome: Double
)