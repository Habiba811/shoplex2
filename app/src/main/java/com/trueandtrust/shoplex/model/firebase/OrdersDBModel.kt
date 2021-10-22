package com.trueandtrust.shoplex.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.trueandtrust.shoplex.model.enumurations.OrderStatus
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.OrdersListener
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.model.pojo.Product

class OrdersDBModel(val listener: OrdersListener) {

    fun getCurrentOrders() {
        FirebaseReferences.ordersRef.whereEqualTo("storeID", StoreInfo.storeID)
            .whereEqualTo("orderStatus", OrderStatus.Current.name)
            .addSnapshotListener { values, _ ->
                if (values != null) {
                    val orders = arrayListOf<Order>()
                    for (document: DocumentSnapshot in values.documents) {
                        val order: Order? = document.toObject<Order>()
                        if (order != null) {
                            orders.add(order)
                            FirebaseReferences.productsRef.document(order.productID).get()
                                .addOnSuccessListener {
                                    order.product = it.toObject<Product>()
                                    if (document == values.last()) {
                                        this.listener.onCurrentOrderReady(orders)
                                    }
                                }
                        }
                    }
                }

            }
    }

    fun getLastOrders(lastID: String) {
        FirebaseReferences.ordersRef.whereEqualTo("storeID", StoreInfo.storeID)
            .whereGreaterThan("orderID", lastID)
            .whereIn("orderStatus", listOf(OrderStatus.Delivered.name, OrderStatus.Canceled.name))
            .addSnapshotListener { values, _ ->
                if (values != null) {
                    val orders = arrayListOf<Order>()
                    for (document: DocumentSnapshot in values.documents) {
                        val order: Order? = document.toObject<Order>()
                        if (order != null) {
                            FirebaseReferences.productsRef.document(order.productID)
                                .get()
                                .addOnSuccessListener {
                                    order.product = it.toObject<Product>()
                                    orders.add(order)
                                    if (document == values.last()) {
                                        this.listener.onLastOrderReady(orders)
                                    }
                                }
                        }
                    }
                }
            }
    }

    companion object {
        fun deliverOrder(orderID: String) {
            FirebaseReferences.ordersRef.document(orderID)
                .update("orderStatus", OrderStatus.Delivered)
        }
    }
}