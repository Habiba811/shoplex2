package com.trueandtrust.shoplex.model.pojo

import java.util.*

class Review {

    var productID : String = ""
    var customerName : String = ""
    var image : String? = null
    var comment : String = ""
    lateinit var date : Date
    var rate : Float = 0.0F

    constructor(
        customerName: String,
        image: String? = null,
        comment: String,
        date: Date,
        rate: Float
    ) {
        this.customerName = customerName
        this.image= image
        this.comment = comment
        this.date = date
        this.rate = rate
    }

    constructor(
        customerName: String,
        image: String? = null,
        productId :String,
        comment: String,
        date: Date,
        rate: Float
    ) {
        this.customerName = customerName
        this.image = image
        this.productID = productId
        this.comment = comment
        this.date = date
        this.rate = rate
    }
    constructor()
}