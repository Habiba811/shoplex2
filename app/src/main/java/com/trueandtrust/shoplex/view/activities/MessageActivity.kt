package com.trueandtrust.shoplex.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.toObject
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityMessageBinding
import com.trueandtrust.shoplex.databinding.DialogAddSaleBinding
import com.trueandtrust.shoplex.model.adapter.ChatHeadAdapter
import com.trueandtrust.shoplex.model.adapter.LeftMessageItem
import com.trueandtrust.shoplex.model.adapter.RightMessageItem
import com.trueandtrust.shoplex.model.adapter.SalesAdapter
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.pojo.Message
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.room.viewModel.MessageFactoryModel
import com.trueandtrust.shoplex.room.viewModel.MessageViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private val messageAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var chatID: String
    private lateinit var userID: String
    private var productsIDs: ArrayList<String>? = null
    private var position: Int = -1
    private lateinit var messageVM: MessageViewModel
    lateinit var salesBtnSheetDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMessage)
        supportActionBar?.apply {
            title = ""
           // setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        val userName = intent.getStringExtra(ChatHeadAdapter.CHAT_TITLE_KEY)
        val productImg = intent.getStringExtra(ChatHeadAdapter.CHAT_IMG_KEY)
        chatID = intent.getStringExtra(ChatHeadAdapter.CHAT_ID_KEY).toString()
        userID = intent.getStringExtra(ChatHeadAdapter.USER_ID_KEY).toString()
        productsIDs = intent.getStringArrayListExtra(ChatHeadAdapter.PRODUCTS_IDS)

        messageVM = ViewModelProvider(
            this,
            MessageFactoryModel(this, chatID)
        ).get(MessageViewModel::class.java)

        messageVM.isOnline.observe(this, {
            if(it){
                binding.cardIsOnline.visibility = View.VISIBLE
            }else{
                binding.cardIsOnline.visibility = View.INVISIBLE
            }
        })

        binding.imgToolbarChat.setImageResource(R.drawable.init_img)
        binding.tvToolbarUserChat.text = userName
        Glide.with(this).load(productImg).into(binding.imgToolbarChat)

        getAllMessage()

        binding.btnSendMessage.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage() {
        //send Message to Firebase
        val messageText = binding.edSendMesssage.text
        if(messageText.trim().isEmpty())
            return
        val message = Message(toId = userID, message = messageText.toString())
        message.chatID = chatID
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .document(message.messageID)
            .set(message).addOnSuccessListener {
                messageAdapter.add(RightMessageItem(message, messageVM))
                messageText.clear()
                binding.rcMessage.smoothScrollToPosition(messageAdapter.groupCount)
            }
    }

    private fun listenToNewMessages(lastID: String) {
        var firstTimeToLoadMessages = (lastID == "1")
        FirebaseReferences.chatRef.document(chatID).collection("messages")
            .whereGreaterThan("messageID", lastID).addSnapshotListener { snapshots, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                for ((index, dc) in snapshots!!.documentChanges.withIndex()) {
                    if ((dc.type) == DocumentChange.Type.ADDED) {
                        val message = dc.document.toObject<Message>()
                        if (message.toId == StoreInfo.storeID) {
                            message.chatID = chatID
                            if (!message.isSent) {
                                FirebaseReferences.chatRef.document(chatID).collection("messages")
                                    .document(message.messageID).update("isSent", true)
                                message.isSent = true
                            }

                            if (!message.isRead && position == -1)
                                position = messageAdapter.groupCount + index - 1

                            messageAdapter.add(LeftMessageItem(chatID, message, messageVM))
                            messageVM.addMessage(message)
                        } else if (message.toId != StoreInfo.storeID) {
                            if (firstTimeToLoadMessages)
                                messageAdapter.add(RightMessageItem(message, messageVM))
                            message.chatID = chatID
                            messageVM.addMessage(message)
                        }
                    }
                }

                firstTimeToLoadMessages = false

                if (position > 0){
                    binding.rcMessage.smoothScrollToPosition(position)
                    position = 0
                }
            }
    }

    private fun getAllMessage() {
        binding.rcMessage.adapter = messageAdapter
        messageVM.readAllMessage.observe(this, {
            for (message in it) {
                if (message.toId == StoreInfo.storeID) {
                    messageAdapter.add(LeftMessageItem(chatID, message, messageVM))
                } else if (message.toId != StoreInfo.storeID) {
                    messageAdapter.add(RightMessageItem(message, messageVM))
                }
            }
            val lastID = if (it.isEmpty()) {
                "1"
            } else {
                binding.rcMessage.smoothScrollToPosition(it.count() - 1)
                it.last().messageID
            }
            messageVM.readAllMessage.removeObservers(this)
            listenToNewMessages(lastID)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.sale -> {
                if (productsIDs != null) {
                    FirebaseReferences.productsRef.whereIn("productID", productsIDs!!).get()
                        .addOnCompleteListener {
                            val products: ArrayList<Product> = arrayListOf()
                            for (product in it.result) {
                                products.add(product.toObject())
                                if (product == it.result.last()) {
                                    openSnackBar(products)
                                }
                            }
                        }
                }
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSnackBar(products: ArrayList<Product>) {
        val binding = DialogAddSaleBinding.inflate(layoutInflater)
        salesBtnSheetDialog = BottomSheetDialog(binding.root.context)

        val salesProductAdapter = SalesAdapter(products, userID)
        binding.rcSalesProduct.adapter = salesProductAdapter

        salesBtnSheetDialog.setContentView(binding.root)
        salesBtnSheetDialog.show()
    }
}



