package com.trueandtrust.shoplex.model.adapter

import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ChatItemLeftBinding
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.pojo.Message
import com.trueandtrust.shoplex.room.viewModel.MessageViewModel
import com.xwray.groupie.databinding.BindableItem

class LeftMessageItem(private val chatID: String, val message: Message, private val messageVM: MessageViewModel) : BindableItem<ChatItemLeftBinding>() {

    override fun bind(binding: ChatItemLeftBinding, position: Int) {
        binding.message = message
        if (!message.isRead) {
            FirebaseReferences.chatRef.document(chatID).collection("messages")
                .document(message.messageID).update("isRead", true)
            messageVM.setRead(message.messageID)
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_item_left
    }
}