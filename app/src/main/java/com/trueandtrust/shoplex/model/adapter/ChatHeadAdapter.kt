package com.trueandtrust.shoplex.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ChatHeadItemRowBinding
import com.trueandtrust.shoplex.model.pojo.ChatHead
import com.trueandtrust.shoplex.view.activities.MessageActivity

class ChatHeadAdapter(private val chatHeads: ArrayList<ChatHead>):
    RecyclerView.Adapter<ChatHeadAdapter.ChatHeadViewHolder>() {

    companion object {
        const val CHAT_TITLE_KEY = "CHAT_TITLE_KEY"
        const val CHAT_IMG_KEY = "CHAT_IMG_KEY"
        const val CHAT_ID_KEY  = "CHAT_ID_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
        const val PRODUCTS_IDS = "PRODUCTS_IDS"
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatHeadViewHolder {
        return ChatHeadViewHolder(
            ChatHeadItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ChatHeadViewHolder, position: Int) = viewHolder.bind(chatHeads[position])

    override fun getItemCount() = chatHeads.size

    inner class ChatHeadViewHolder(val binding: ChatHeadItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatHead: ChatHead) {
            Glide.with(itemView.context).load(chatHead.productImageURL).error(R.drawable.init_img).into(binding.imgChatHead)

            binding.chatHead = chatHead

            if(chatHead.numOfMessage > 0)
                binding.tvNumOfMessage.visibility = View.VISIBLE
            else
                binding.tvNumOfMessage.visibility = View.INVISIBLE

            if(chatHead.isClientOnline)
                binding.cardImg.visibility = View.VISIBLE
            else
                binding.cardImg.visibility = View.INVISIBLE

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MessageActivity::class.java)
                intent.putExtra(CHAT_TITLE_KEY, chatHead.userName)
                intent.putExtra(CHAT_IMG_KEY,chatHead.productImageURL)
                intent.putExtra(CHAT_ID_KEY,chatHead.chatId)
                intent.putExtra(USER_ID_KEY,chatHead.userID)
                intent.putExtra(PRODUCTS_IDS, chatHead.productsIDs)
                itemView.context.startActivity(intent)
            }
        }
    }
}
