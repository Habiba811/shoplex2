package com.trueandtrust.shoplex.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.trueandtrust.shoplex.model.enumurations.DeliveryMethod
import com.trueandtrust.shoplex.model.enumurations.OrderStatus
import com.trueandtrust.shoplex.model.enumurations.PaymentMethod

@Entity(tableName = "orders")
data class Order (
    @PrimaryKey
    var orderID: String = "",
    var productID: String = "",
    var userID: String = "",
    var storeID: String = "",
    var storeName: String = "",
    var orderStatus: OrderStatus = OrderStatus.Current,
    var quantity: Int = 1,
    var specialDiscount: SpecialDiscount? = null,

    var productPrice: Float = 0.0F,
    var subTotalPrice: Float = 0F,
    var totalDiscount: Float = 0F,
    var shipping: Float = 0F,
    var totalPrice: Float = 0F,

    @Exclude
    @set:Exclude @get:Exclude
    var product:Product? = null,

    var deliveryMethod: String = DeliveryMethod.Door.name,
    var paymentMethod: String = PaymentMethod.Cash.name,
    var deliveryLoc: Location? = null,
    var deliveryAddress: String = "",
    val orderProperties: ArrayList<String>? = null,

)