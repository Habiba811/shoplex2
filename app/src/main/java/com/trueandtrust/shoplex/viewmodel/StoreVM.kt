package com.trueandtrust.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.firebase.StoreDBModel
import com.trueandtrust.shoplex.model.interfaces.StoreListener
import com.trueandtrust.shoplex.model.pojo.Location
import com.trueandtrust.shoplex.model.pojo.PendingLocation

class StoreVM: ViewModel(), StoreListener {

    private var storeDBModel: StoreDBModel = StoreDBModel(this)
    var storeAddresses: MutableLiveData<ArrayList<PendingLocation>> = MutableLiveData()
    var isLocationRemoved: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllAddresses(){
        val storeLocationAddresses = StoreInfo.addresses.zip(StoreInfo.locations)
        val storeLocations: ArrayList<PendingLocation> = arrayListOf()
        for (storeLocation in storeLocationAddresses){
            val location = PendingLocation(address=storeLocation.first, location = storeLocation.second)
            storeLocations.add(location)
        }
        storeAddresses.value = storeLocations
    }

    fun addStoreLocation(pendingLocation: PendingLocation){
        storeDBModel.addStoreLocation(pendingLocation)
    }

    fun removeLocationAddress(location: PendingLocation){
        storeDBModel.removeLocationAddress(address = location.address, location = location.location)
    }

    override fun onRemoveLocationSuccess(address: String, location: Location) {
        StoreInfo.addresses.remove(address)
        val loc = StoreInfo.locations.find {
            it.latitude == location.latitude && it.longitude == location.longitude
        }

        StoreInfo.locations.remove(loc)
        isLocationRemoved.value = true
    }
}