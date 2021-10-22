package com.trueandtrust.shoplex.model.extra

import android.annotation.SuppressLint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object FirebaseReferences {
    // -----------------------> Databases <----------------------- //
    @SuppressLint("StaticFieldLeak")
    private val database = Firebase.firestore
    private val imagesDatabase = FirebaseStorage.getInstance().reference

    // -----------------------> Firestore <----------------------- //
    // Products
    val pendingProductsRef = database.collection("Pending Products")
    val pendingSellersRef = database.collection("PendingSellers")
    val productsRef = database.collection("Products")
    val pendingLocationsRef = database.collection("Pending Locations")
    val storeRef = database.collection("Sellers")
    val recentVisits = database.collection("Recent Visits")
    val chatRef = database.collection("Chats")
    val ordersRef = database.collection("Orders")
    val ReportRef = database.collection("Reports")

    // -----------------------> Storage <----------------------- //
    // Products
    val imagesProductsRef = imagesDatabase.child("Products")
    val imagesStoreRef = imagesDatabase.child("Sellers")

    //----------------------> Notifications <---------------------------//
    val notificationTokensRef = database.collection("Notification Center")
}