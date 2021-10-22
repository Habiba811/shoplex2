package com.trueandtrust.shoplex.model.interfaces

import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.model.pojo.Review
import com.trueandtrust.shoplex.model.pojo.ReviewStatistics

interface ProductsListener {

    fun onAllProductsReady(products: ArrayList<Product>){}
    fun onProductAdded(){}
    fun onProductRemoved(){}

    fun onAllReviewsReady(reviews:ArrayList<Review>){}
    fun onReviewStatisticsReady(reviewStatistics: ReviewStatistics){}
}