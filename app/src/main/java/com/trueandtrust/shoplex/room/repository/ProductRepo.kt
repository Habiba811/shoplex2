package com.trueandtrust.shoplex.room.repository

import androidx.lifecycle.LiveData
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.room.data.StoreDao

class ProductRepo(private val productDao: StoreDao) {

    val readAllProduct: LiveData<List<Product>> = productDao.readAllProducts()

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }
}