package com.trueandtrust.shoplex.model.interfaces

import com.trueandtrust.shoplex.model.pojo.ChatHead

interface ChatsListener {
    fun onChatHeadsReady(chatHeads: ArrayList<ChatHead>){}
    fun onChatHeadChanged(position: Int){}
}