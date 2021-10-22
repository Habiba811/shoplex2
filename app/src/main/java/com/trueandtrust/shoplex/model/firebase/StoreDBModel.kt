package com.trueandtrust.shoplex.model.firebase

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.StoreListener
import com.trueandtrust.shoplex.model.pojo.Location
import com.trueandtrust.shoplex.model.pojo.PendingLocation
import com.trueandtrust.shoplex.model.pojo.Store

class StoreDBModel(val listener: StoreListener) {

    fun addStoreLocation(pendingLocation: PendingLocation){
        FirebaseReferences.pendingLocationsRef.add(pendingLocation)
    }

    fun  removeLocationAddress(address: String, location: Location){
        FirebaseReferences.storeRef.document(StoreInfo.storeID.toString()).update("addresses", FieldValue.arrayRemove(address))
        FirebaseReferences.storeRef.document(StoreInfo.storeID.toString()).update("locations", FieldValue.arrayRemove(location)).addOnSuccessListener {
            listener.onRemoveLocationSuccess(address, location)
        }
    }
}