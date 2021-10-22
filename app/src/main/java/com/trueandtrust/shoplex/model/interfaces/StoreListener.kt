package com.trueandtrust.shoplex.model.interfaces

import com.trueandtrust.shoplex.model.pojo.Location

interface StoreListener {
    fun onRemoveLocationSuccess(address: String, location: Location){}
}
