package com.trueandtrust.shoplex.model.pojo

import com.google.firebase.Timestamp
import com.trueandtrust.shoplex.model.extra.StoreInfo

data class RecentVisit(
    val ID: String = StoreInfo.storeID!!,
    val name:String = StoreInfo.name,
    val email: String = StoreInfo.email,
    val image: String = StoreInfo.image!!,
    var address: String = StoreInfo.addresses[0],
    val phone: String = StoreInfo.phone,
    val date: Timestamp = Timestamp.now(),
    val type: String = "Store"
)