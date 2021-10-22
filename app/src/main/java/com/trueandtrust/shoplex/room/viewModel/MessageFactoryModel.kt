package com.trueandtrust.shoplex.room.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MessageFactoryModel(val context: Context, val chatID: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MessageViewModel(context,chatID) as T
}