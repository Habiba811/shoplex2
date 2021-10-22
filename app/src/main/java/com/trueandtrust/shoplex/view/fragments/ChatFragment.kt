package com.trueandtrust.shoplex.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.FragmentChatBinding
import com.trueandtrust.shoplex.model.adapter.ChatHeadAdapter
import com.trueandtrust.shoplex.viewmodel.ChatsVM

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatsVm: ChatsVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        chatsVm = ViewModelProvider(requireActivity()).get(ChatsVM::class.java)

        if (chatsVm.chatHeads.value == null)
            chatsVm.getChatHeads()

        requireActivity().title = getString(R.string.chat)

        chatsVm.chatHeads.observe(viewLifecycleOwner, { chatHeads ->
            if (chatHeads.count()>0) {
                binding.noItem.visibility=View.INVISIBLE
            }
            else{
                binding.noItem.visibility=View.VISIBLE
            }
            binding.rvChat.adapter = ChatHeadAdapter(chatHeads)
        })

        chatsVm.changedPosition.observe(viewLifecycleOwner, {
            binding.rvChat.adapter?.notifyItemChanged(it)
        })

        return binding.root
    }
}