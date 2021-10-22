package com.trueandtrust.shoplex.model.extra

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.droidnet.DroidListener
import com.droidnet.DroidNet

class ArchLifecycleApp : Application(), LifecycleObserver, DroidListener {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        DroidNet.init(this)
        DroidNet.getInstance().addInternetConnectivityListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        if (StoreInfo.storeID != null) {
            FirebaseReferences.chatRef.whereEqualTo("storeID", StoreInfo.storeID!!).get().addOnSuccessListener {
                for(ref in it.documents)
                    ref.reference.update("isStoreOnline", true)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        if (StoreInfo.storeID != null) {
            FirebaseReferences.chatRef.whereEqualTo("storeID", StoreInfo.storeID!!).get().addOnSuccessListener {
                for(ref in it.documents)
                    ref.reference.update("isStoreOnline", false)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppDestroy(){
        if (StoreInfo.storeID != null) {
            FirebaseReferences.chatRef.whereEqualTo("storeID", StoreInfo.storeID!!).get().addOnSuccessListener {
                for(ref in it.documents)
                    ref.reference.update("isStoreOnline", false)
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        isInternetConnected = isConnected
    }

    companion object{
        var isInternetConnected: Boolean = true
    }
}