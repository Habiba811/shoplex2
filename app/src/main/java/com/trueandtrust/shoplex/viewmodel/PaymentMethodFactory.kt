package com.trueandtrust.shoplex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trueandtrust.shoplex.model.interfaces.PaymentListener

class PaymentMethodFactory(
    val context: Context,
    val listener: PaymentListener
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        PaymentMethodVM(context, listener) as T
}