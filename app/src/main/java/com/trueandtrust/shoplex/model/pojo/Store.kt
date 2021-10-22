package com.trueandtrust.shoplex.model.pojo

import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList

data class Store(
    var storeID : String = UUID.randomUUID().toString(),
    var name : String = "",
    var email : String = "",
    var image : String = "",
    var locations : ArrayList<Location> = arrayListOf(),
    var addresses : ArrayList<String> = arrayListOf(),
    var phone : String = "",
    @ServerTimestamp var date: Date? = null,
    val notification: Boolean = true
)