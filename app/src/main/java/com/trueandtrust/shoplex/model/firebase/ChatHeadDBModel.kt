package com.trueandtrust.shoplex.model.firebase

import com.google.firebase.firestore.ktx.toObject
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.ChatsListener
import com.trueandtrust.shoplex.model.pojo.Chat
import com.trueandtrust.shoplex.model.pojo.ChatHead
import com.trueandtrust.shoplex.model.pojo.Product

class ChatHeadDBModel(private val listener: ChatsListener) {

    fun getChatHeads() {
        val chatHeads = arrayListOf<ChatHead>()
        FirebaseReferences.chatRef.whereEqualTo("storeID", StoreInfo.storeID).get().addOnSuccessListener { result ->
//        if(result == null || error != null)
//            return@addSnapshotListener
            //chatHeads.clear()

                for (document in result.documents) {
                    if (document != null) {
                        val chat: Chat = document.toObject()!!
                        FirebaseReferences.productsRef
                            .document(chat.productIDs.last()).get()
                            .addOnSuccessListener { productDocument ->
                                if (productDocument != null) {
                                    val product: Product? = productDocument.toObject()
                                    if (product != null) {
                                        val chatHead = ChatHead(
                                            chat.productIDs,
                                            product.storeID,
                                            chat.chatID,
                                            product.name,
                                            product.price,
                                            product.images.firstOrNull(),
                                            chat.userID,
                                            chat.userName,
                                            chat.unreadStoreMessages,
                                            isClientOnline = chat.isClientOnline
                                        )
                                        setListener(chatHead, chatHeads.size)
                                        chatHeads.add(chatHead)

                                        if (document.equals(result.last())) {
                                            this.listener.onChatHeadsReady(chatHeads)
                                        }
                                    }
                                }
                            }
                    }
                }
            }
    }

    private fun setListener(chatHead: ChatHead, position: Int) {
        FirebaseReferences.chatRef.document(chatHead.chatId).addSnapshotListener { value, error ->
            if (error != null)
                return@addSnapshotListener

            if (value != null) {
                val chat: Chat = value.toObject()!!
                if (chat.productIDs.size != chatHead.productsIDs.size) {
                    FirebaseReferences.productsRef
                        .document(chat.productIDs.last()).get()
                        .addOnSuccessListener { productDocument ->
                            if (productDocument != null) {
                                val product = productDocument.toObject<Product>()!!
                                chatHead.productsIDs = chat.productIDs
                                chatHead.storeId = product.storeID
                                chatHead.productName = product.name
                                chatHead.price = product.price
                                chatHead.productImageURL = product.images.first()
                                chatHead.numOfMessage = chat.unreadStoreMessages
                            }
                        }
                }
                chatHead.isClientOnline = chat.isClientOnline
                chatHead.numOfMessage = chat.unreadStoreMessages
                listener.onChatHeadChanged(position)
            }
        }
    }
}