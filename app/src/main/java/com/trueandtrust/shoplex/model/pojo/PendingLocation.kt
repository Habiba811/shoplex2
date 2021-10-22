package com.trueandtrust.shoplex.model.pojo

import com.trueandtrust.shoplex.model.extra.StoreInfo

data class PendingLocation(var storeID: String = StoreInfo.storeID!!, var storeName: String = StoreInfo.name, var address: String, var location:Location)
