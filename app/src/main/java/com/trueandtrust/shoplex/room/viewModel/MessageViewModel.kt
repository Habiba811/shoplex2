package com.trueandtrust.shoplex.room.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.pojo.Chat
import com.trueandtrust.shoplex.model.pojo.Message
import com.trueandtrust.shoplex.room.data.ShopLexDatabase
import com.trueandtrust.shoplex.room.repository.MessageRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel(context: Context, val chatID : String) : ViewModel() {
    var readAllMessage : LiveData<List<Message>> = MutableLiveData()
    private val messageRepo : MessageRepo
    var isOnline: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val messageDao = ShopLexDatabase.getDatabase(context).storeDao()
        messageRepo = MessageRepo(messageDao,chatID)
        readAllMessage = messageRepo.readAllMessage
        listenToOnlineUser()
    }

    fun addMessage(rightMessage: Message){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepo.addMessage(rightMessage)
        }
    }

    fun setSent(messageID: String){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepo.setSent(messageID)
        }
    }

    fun setRead(messageID: String){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepo.setReadMessage(messageID)
        }
    }

    private fun listenToOnlineUser(){
        FirebaseReferences.chatRef.document(chatID).addSnapshotListener { value, error ->
            if (error != null)
                return@addSnapshotListener

            if (value != null) {
                val chat: Chat = value.toObject()!!
                this.isOnline.value = chat.isClientOnline
            }
        }
    }
}