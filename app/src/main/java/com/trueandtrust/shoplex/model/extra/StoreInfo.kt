package com.trueandtrust.shoplex.model.extra

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trueandtrust.shoplex.model.pojo.*
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

object StoreInfo {
    private const val SHARED_STORE_INFO = "STORE_INFO"
    var storeID: String? = null
    var name: String = ""
    var email: String = ""
    var image: String? = null
    var locations: ArrayList<Location> = arrayListOf()
    var addresses: ArrayList<String> = arrayListOf()
    var phone: String = ""
    var lang: String = "en"

    fun updateTokenID() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener

            val token = task.result
            val notificationToken = NotificationToken(this.storeID!!, token.toString())
            FirebaseReferences.notificationTokensRef.document(this.storeID!!).set(notificationToken)
        })
    }

    fun saveStoreInfo(context: Context) {
        context.getSharedPreferences(SHARED_STORE_INFO, Context.MODE_PRIVATE).edit()
            .putString("storeID", storeID)
            .putString("name", name)
            .putString("email", email)
            .putString("image", image)
            .putString("phone", phone)
            .putString("locations", Gson().toJson(locations))
            .putString("addresses", Gson().toJson(addresses))
            .apply()
    }

    fun readStoreInfo(context: Context) {
        lang =
            context.getSharedPreferences("LANG", Context.MODE_PRIVATE).getString("Language", "en")
                ?: "en"
        val sharedPref = context.getSharedPreferences(SHARED_STORE_INFO, Context.MODE_PRIVATE)
        storeID = sharedPref.getString("storeID", null)
        if (storeID == null)
            return
        name = sharedPref.getString("name", "")!!
        email = sharedPref.getString("email", "")!!
        image = sharedPref.getString("image", null)
        phone = sharedPref.getString("phone", "")!!
        val addressesType: Type = object : TypeToken<ArrayList<String>>() {}.type
        val locationsType: Type = object : TypeToken<ArrayList<Location>>() {}.type
        addresses = Gson().fromJson(sharedPref.getString("addresses", null), addressesType)
        locations = Gson().fromJson(sharedPref.getString("locations", null), locationsType)
    }

    fun clearSharedPref(context: Context) {
        context.getSharedPreferences(SHARED_STORE_INFO, Context.MODE_PRIVATE).edit()
            .remove("storeID")
            .remove("name")
            .remove("email")
            .remove("image")
            .remove("phone")
            .remove("locations")
            .remove("addresses")
            .apply()
    }

    fun addStoreLocation(context: Context, pendingLocation: PendingLocation) {
        addresses.add(pendingLocation.address)
        locations.add(pendingLocation.location)
        context.getSharedPreferences(SHARED_STORE_INFO, Context.MODE_PRIVATE).edit()
            .putString("locations", Gson().toJson(locations))
            .putString("addresses", Gson().toJson(addresses))
            .apply()
    }

    fun saveNotification(context: Context, value: Boolean) {
        if(storeID !=null) {


            FirebaseReferences.notificationTokensRef.document(storeID!!)
                .update("notification", value)
            context.getSharedPreferences(SHARED_STORE_INFO, Context.MODE_PRIVATE).edit()
                .putBoolean("notification", value).apply()
        }
    }

    fun readNotification(context: Context): Boolean {
        val shared = context.getSharedPreferences(
            SHARED_STORE_INFO,
            Context.MODE_PRIVATE
        )
        return shared.getBoolean("notification", true)
    }

    fun saveToRecentVisits() {
        FirebaseReferences.recentVisits.add(RecentVisit())
    }

    fun setLocale(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
        val activity: AppCompatActivity = context as AppCompatActivity
        val refresh = Intent(activity, activity::class.java)
        activity.finish()
        activity.startActivity(refresh)
    }

    fun clear() {
        this.storeID = null
        this.name = ""
        this.email = ""
        this.image = null
        this.locations = arrayListOf()
        this.addresses = arrayListOf()
        this.phone = ""
    }
}