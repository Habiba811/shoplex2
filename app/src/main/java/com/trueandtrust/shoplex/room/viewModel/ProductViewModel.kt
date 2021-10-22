package com.trueandtrust.shoplex.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.room.data.ShopLexDatabase
import com.trueandtrust.shoplex.room.repository.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllProduct: LiveData<List<Product>>
    private val productRepo:ProductRepo

    init {
        val productDao = ShopLexDatabase.getDatabase(application).storeDao()
        productRepo = ProductRepo(productDao)
        readAllProduct = productRepo.readAllProduct
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.addProduct(product)
        }
    }

    fun updateProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            productRepo.updateProduct(product)
        }
    }
}