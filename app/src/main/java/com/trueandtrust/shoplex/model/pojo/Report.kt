package com.trueandtrust.shoplex.model.pojo

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Report(val reportComment : String = "", @ServerTimestamp val date : Date? = null, val type : String = "Seller")