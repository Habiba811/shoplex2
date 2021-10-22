package com.trueandtrust.shoplex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trueandtrust.shoplex.model.firebase.ProductsDBModel
import com.trueandtrust.shoplex.model.interfaces.ProductsListener
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.model.pojo.Review
import com.trueandtrust.shoplex.model.pojo.ReviewStatistics

class ProductsVM: ViewModel(), ProductsListener {

    private var productsDBModel = ProductsDBModel(this)

    var products: MutableLiveData<ArrayList<Product>> = MutableLiveData()
    var reviews: MutableLiveData<ArrayList<Review>> = MutableLiveData()
    val reviewStatistics: MutableLiveData<ReviewStatistics> = MutableLiveData()

    fun getAllProducts() {
        productsDBModel.getAllProducts()
    }

    fun getReviewsByProductId(productId: String) {
        productsDBModel.getReviewsStatistics(productId)
        productsDBModel.getReviewByProductId(productId)
    }

    override fun onAllProductsReady(products: ArrayList<Product>) {
        this.products.value = products
    }

    override fun onAllReviewsReady(reviews: ArrayList<Review>) {
        this.reviews.value = reviews
    }

    override fun onReviewStatisticsReady(reviewStatistics: ReviewStatistics) {
        this.reviewStatistics.value = reviewStatistics
    }
}