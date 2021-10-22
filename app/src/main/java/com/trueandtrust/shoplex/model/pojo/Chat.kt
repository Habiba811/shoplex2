package com.trueandtrust.shoplex.model.pojo

import com.google.firebase.firestore.Exclude
import com.trueandtrust.shoplex.model.extra.StoreInfo

data class Chat(
    var chatID: String = "",
    val userID: String = "",
    val userName: String = "",
    val storeID: String = "",
    var productIDs: ArrayList<String> = arrayListOf(String()),
    val unreadStoreMessages: Int = 0,
    @Exclude @set:Exclude
    var unreadCustomerMessages: Int = 0,
    @JvmField
    val isStoreOnline: Boolean = false,
    @JvmField
    val isClientOnline: Boolean = false,
    val storePhone: String = StoreInfo.phone
)