package com.trueandtrust.shoplex.model.interfaces

import android.content.Context
import android.widget.Toast
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.pojo.Store
import com.trueandtrust.shoplex.room.data.ShopLexDatabase

interface AuthListener {
    fun showIndicator(){}
    fun hideIndicator(){}

    private fun onStoreInfoReady(context: Context, store: Store){


        StoreInfo.storeID = store.storeID
        StoreInfo.name = store.name
        StoreInfo.email = store.email
        StoreInfo.image = store.image
        StoreInfo.locations = store.locations
        StoreInfo.addresses = store.addresses
        StoreInfo.phone = store.phone

        StoreInfo.updateTokenID()
        StoreInfo.saveStoreInfo(context)
        StoreInfo.saveToRecentVisits()
        StoreInfo.saveNotification(context, true)
    }

    fun onUserExists(context: Context){
        Toast.makeText(context, "This Registration Email Exist", Toast.LENGTH_SHORT).show()
    }

    fun onPendingStore(){

    }

    fun onLoginSuccess(context: Context, store: Store){
        onStoreInfoReady(context, store)
    }

    fun onLoginFailed(){
        StoreInfo.clear()
    }

    fun onAddStoreFailed(){
    }
}