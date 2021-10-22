package com.trueandtrust.shoplex.model.pojo

import androidx.room.Entity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.util.*

@Entity(tableName = "message", primaryKeys = ["messageID","chatID"])
data class Message(
    var messageID: String = Timestamp.now().toDate().time.toString(),
    var messageDate: Date = Timestamp.now().toDate(),
    val toId: String? = "",
    var message: String = "",
    @field:JvmField var isSent: Boolean = false,
    @field:JvmField val isRead: Boolean = false,
    @Exclude @set:Exclude @get:Exclude var chatID:String = ""
)