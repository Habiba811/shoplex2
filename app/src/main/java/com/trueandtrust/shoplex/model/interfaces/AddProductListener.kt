package com.trueandtrust.shoplex.model.interfaces

import com.trueandtrust.shoplex.model.pojo.Property

interface AddProductListener {
    fun onImageRemoved(position: Int){}
    fun onNewPropertyAdded(property: Property){}
    fun onPropertyRemoved(position: Int){}
}