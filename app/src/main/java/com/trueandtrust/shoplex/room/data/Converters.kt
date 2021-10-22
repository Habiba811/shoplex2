package com.trueandtrust.shoplex.room.data

import android.net.Uri
import androidx.room.TypeConverter
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.model.LatLng
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.trueandtrust.shoplex.model.pojo.*
import java.lang.reflect.Type
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromUri(value: String?): ArrayList<Uri?>? {
        val listType: Type = object : TypeToken<ArrayList<Uri?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toUri(list: ArrayList<Uri?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromProperty(value: String?): ArrayList<Property?>? {
        val listType: Type = object : TypeToken<ArrayList<Property?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toProperty(list: ArrayList<Property?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
    @TypeConverter
    fun fromSlideModel(value: String?): ArrayList<SlideModel?>? {
        val listType: Type = object : TypeToken<ArrayList<SlideModel?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toSlideModel(list: ArrayList<SlideModel?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToModel(json: String?): LatLng? {
        val gson = Gson()
        val type = object : TypeToken<LatLng?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun modelToString(position: LatLng?): String? {
        val gson = Gson()
        val type = object : TypeToken<LatLng?>() {}.type
        return gson.toJson(position, type)
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return try {
            Gson().fromJson(locationString, Location::class.java)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toLocationString(location: Location?): String? {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun sdToString(specialDiscount: Premium?): String? {
        if(specialDiscount != null)
            return Gson().toJson(specialDiscount)
        return null
    }

    @TypeConverter
    fun stringToSd(string: String?): Premium? = Gson().fromJson(string, Premium::class.java)


//    @TypeConverter // note this annotation
//    fun fromOptionValuesList(optionValues: List<OptionValues?>?): String? {
//        if (optionValues == null) {
//            return null
//        }
//        val gson = Gson()
//        val type = object :
//            TypeToken<List<OptionValues?>?>() {}.type
//        return gson.toJson(optionValues, type)
//    }
//
//    @TypeConverter // note this annotation
//    fun toOptionValuesList(optionValuesString: String?): List<OptionValues?>? {
//        if (optionValuesString == null) {
//            return null
//        }
//        val gson = Gson()
//        val type = object :
//            TypeToken<List<OptionValues?>?>() {}.type
//        return gson.fromJson(optionValuesString, type)
//    }

    @TypeConverter
    fun specialDiscountToString(specialDiscount: SpecialDiscount?): String? {
        if(specialDiscount != null)
            return Gson().toJson(specialDiscount)
        return null
    }

    @TypeConverter
    fun stringToSpecialDiscoun(string: String?): SpecialDiscount? = Gson().fromJson(string, SpecialDiscount::class.java)

    @TypeConverter
    fun productToString(product: Product?): String? {
        if(product != null)
            return Gson().toJson(product)
        return null
    }

    @TypeConverter
    fun stringToProduct(string: String?): Product? = Gson().fromJson(string, Product::class.java)

}