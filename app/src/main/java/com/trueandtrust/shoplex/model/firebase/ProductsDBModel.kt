package com.trueandtrust.shoplex.model.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.trueandtrust.shoplex.model.extra.FirebaseReferences
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.interfaces.ProductsListener
import com.trueandtrust.shoplex.model.pojo.Location
import com.trueandtrust.shoplex.model.pojo.Product
import com.trueandtrust.shoplex.model.pojo.Review
import com.trueandtrust.shoplex.model.pojo.ReviewStatistics
import java.util.*
import kotlin.collections.ArrayList

class ProductsDBModel(val listener: ProductsListener) {
    private lateinit var product: Product
    private lateinit var context: Context

    private lateinit var reference: CollectionReference
    private var imagesURLs: ArrayList<String> = arrayListOf()

    constructor(
        product: Product,
        context: Context,
        isUpdate: Boolean,
        listener: ProductsListener
    ) : this(listener) {
        this.product = product
        this.context = context

        reference =
            if (isUpdate) FirebaseReferences.productsRef else FirebaseReferences.pendingProductsRef
    }

    fun addUpdateProduct() {
        product.storeLocation = StoreInfo.locations.firstOrNull() ?: Location()
        reference.document(product.productID).set(product).addOnSuccessListener {
            listener.onProductAdded()
        }

        // New Added Images
        for (imgURI in product.imagesListURI)
            if (!product.images.contains(imgURI.path))
                addImage(imgURI)

        // Removed Images
        for (imgURL in product.removedImages)
            deleteImage(imgURL, product.productID)
    }

    private fun addImage(uri: Uri) {
        val imgRef: StorageReference =
            FirebaseReferences.imagesProductsRef.child(product.productID)
                .child(UUID.randomUUID().toString())

        imgRef.putFile(uri).addOnSuccessListener { _ ->
            imgRef.downloadUrl.addOnSuccessListener {
                imagesURLs.add(it.toString())
                if (product.imagesListURI.last() == uri) {
                    for (imgURL in imagesURLs) {
                        reference.document(product.productID).update("images", FieldValue.arrayUnion(imgURL))
                    }
                }
            }
        }
    }

    private fun deleteImage(path: String, productID: String) {
        reference.document(productID)
            .update("images", FieldValue.arrayRemove(path))
        val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(path)
        imageRef.delete()
    }

    fun getAllProducts() {
        FirebaseReferences.productsRef.whereEqualTo("storeID", StoreInfo.storeID)
            .whereEqualTo("deleted", false)
            .addSnapshotListener { values, _ ->
                val products = arrayListOf<Product>()
                for (document: DocumentSnapshot in values?.documents!!) {
                    val product: Product? = document.toObject<Product>()
                    if (product != null) {
                        products.add(product)
                    }

                    if (document == values.documents.last()) {
                        this.listener.onAllProductsReady(products)
                    }
                }
            }
    }

    fun deleteProduct(productID: String) {
        FirebaseReferences.productsRef.document(productID).update("deleted", true)
            .addOnSuccessListener {
                this.listener.onProductRemoved()
            }
    }

    fun getReviewByProductId(productId: String) {

        FirebaseReferences.productsRef.document(productId).collection("Reviews")
            .addSnapshotListener { values, _ ->
                val reviews = arrayListOf<Review>()
                for (document in values?.documents!!) {
                    val review: Review? = document.toObject<Review>()
                    if (review != null) {
                        reviews.add(review)
                    }
                }
                this.listener.onAllReviewsReady(reviews)
            }
    }

    fun getReviewsStatistics(productId: String) {
        FirebaseReferences.productsRef.document(productId).collection("Statistics")
            .document("Reviews").get().addOnSuccessListener {
                if (it.exists()) {
                    val statistic: ReviewStatistics = it.toObject()!!
                    this.listener.onReviewStatisticsReady(statistic)
                }

            }
    }
}