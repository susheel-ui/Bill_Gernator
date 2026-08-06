package com.example.bill_genrating_app.viewModels.Factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_genrating_app.Api.repository.OrderRepo
import com.example.bill_genrating_app.viewModels.ViewOrderViewModel

class ViewOrderViewFactory   (private val application: Application,
private val orderId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewOrderViewModel::class.java)) {
            return ViewOrderViewModel(application, orderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}