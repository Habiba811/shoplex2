package com.trueandtrust.shoplex.model.pojo

data class NotificationToken(val userID: String = "",
                             val tokenID: String = "",
                             val userType: String = "Store",
                             val notification: Boolean = true)