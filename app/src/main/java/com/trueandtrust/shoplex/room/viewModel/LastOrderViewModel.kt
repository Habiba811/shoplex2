package com.trueandtrust.shoplex.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.room.data.ShopLexDatabase
import com.trueandtrust.shoplex.room.repository.LastOrderRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LastOrderViewModel(application: Application) : AndroidViewModel(application) {
    val readAllLastOrder: LiveData<List<Order>>
    private val lastOrderRepo: LastOrderRepo

    init {
        val lastOrderDao = ShopLexDatabase.getDatabase(application).storeDao()
        lastOrderRepo = LastOrderRepo(lastOrderDao)
        readAllLastOrder = lastOrderRepo.readAllLastOrder
    }
    fun addLastOrder(lastOrder: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            lastOrderRepo.addLastOrder(lastOrder)
        }
    }

}
