package com.trueandtrust.shoplex.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trueandtrust.shoplex.model.firebase.OrdersDBModel
import com.trueandtrust.shoplex.model.interfaces.OrdersListener
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.room.data.ShopLexDatabase
import com.trueandtrust.shoplex.room.repository.LastOrderRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersVM(val context: Context) : ViewModel(), OrdersListener {

    private val ordersDBModel = OrdersDBModel(this)
    var order: MutableLiveData<ArrayList<Order>> = MutableLiveData()
    var lastOrders: MutableLiveData<ArrayList<Order>> = MutableLiveData()

    private val lastOrderRepo: LastOrderRepo =
        LastOrderRepo(ShopLexDatabase.getDatabase(context).storeDao())
    private var roomLastOrders = arrayListOf<Order>()

    fun getCurrentOrders() {
        ordersDBModel.getCurrentOrders()
    }

    fun getLastOrders() {
        /*
        lastOrderRepo.readAllLastOrder.observe(context as AppCompatActivity, { orders ->
            var lastID = "1"
            if (orders != null && orders.count() > 0) {
                roomLastOrders = orders as ArrayList<Order>
                lastID = orders.last().orderID
                lastOrders.value = roomLastOrders
            }

            lastOrderRepo.readAllLastOrder.removeObservers(context)
            ordersDBModel.getLastOrders(lastID)
        })
        */

        ordersDBModel.getLastOrders("1")
    }

    override fun onCurrentOrderReady(orders: ArrayList<Order>) {
        this.order.value = orders
    }

    override fun onLastOrderReady(lastOrders: ArrayList<Order>) {
        /*
        for (order in lastOrders) {
            viewModelScope.launch(Dispatchers.IO) {
                lastOrderRepo.addLastOrder(order)
            }
        }*/

        this.lastOrders.value = lastOrders //(roomLastOrders + lastOrders) as ArrayList<Order>
    }
}
