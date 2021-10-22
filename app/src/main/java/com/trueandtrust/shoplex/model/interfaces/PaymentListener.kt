package com.trueandtrust.shoplex.model.interfaces

interface PaymentListener {
    fun onPaymentComplete()
    fun onPaymentFailedToLoad()
    fun onMinimumPrice(price: Float)
    fun onPaymentCanceledOrFailed()
}