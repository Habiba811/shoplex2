package com.trueandtrust.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.room.data.StoreDao

class LastOrderRepo(private val lastOrderDao : StoreDao) {

    val readAllLastOrder : LiveData<List<Order>> = lastOrderDao.readAllLastOrders()

    suspend fun addLastOrder(lastOrder : Order){
       lastOrderDao.addLastOrder(lastOrder)
    }
}
