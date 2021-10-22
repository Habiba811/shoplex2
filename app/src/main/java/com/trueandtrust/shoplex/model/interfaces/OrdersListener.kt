package com.trueandtrust.shoplex.model.interfaces

import com.trueandtrust.shoplex.model.pojo.Order

interface OrdersListener {
    fun onCurrentOrderReady(orders:ArrayList<Order>){}
    fun onLastOrderReady(lastOrders:ArrayList<Order>){}
}