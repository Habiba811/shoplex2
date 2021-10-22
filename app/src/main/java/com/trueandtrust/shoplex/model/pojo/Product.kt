package com.trueandtrust.shoplex.model.pojo

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.trueandtrust.shoplex.model.extra.StoreInfo
import java.util.*

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    var productID: String = UUID.randomUUID().toString(),
    var storeID: String = StoreInfo.storeID!!,
    var storeName: String = StoreInfo.name,
    var storeLocation: Location = Location(),
    var name: String = "",
    var description: String = "",
    var price: Float = 10F,
    var newPrice: Float = 10F,
    var discount: Int = 0,
    var category: String = "",
    var subCategory: String = "",

    var rate: Float? = null,
    var premium: Premium? = null,
    var properties: ArrayList<Property> = arrayListOf(),
    @ServerTimestamp
    var date: Date? = null,
    val deleted: Boolean = false,
    var quantity: Int = 1,
    var sold: Int = 0,

    var images: ArrayList<String?> = arrayListOf(),

    @Exclude
    @set:Exclude
    @get:Exclude
    var removedImages: ArrayList<String> = arrayListOf(),

    @Exclude
    @set:Exclude
    @get:Exclude
    var imagesListURI: ArrayList<Uri> = arrayListOf(),

    @Exclude
    @set:Exclude
    @get:Exclude
    var imageSlideList: ArrayList<SlideModel> = arrayListOf()

) : Parcelable {
    constructor(parcel: Parcel) : this() {
        productID = parcel.readString().toString()
        name = parcel.readString().toString()
        description = parcel.readString().toString()
        price = parcel.readFloat()
        newPrice = parcel.readFloat()
        discount = parcel.readInt()
        category = parcel.readString().toString()
        subCategory = parcel.readString().toString()
        premium = parcel.readParcelable(Premium::class.java.classLoader)
        parcel.readStringList(removedImages)
        parcel.readStringList(images)
        imagesListURI = parcel.readArrayList(Uri::class.java.classLoader) as ArrayList<Uri>
        properties = parcel.readArrayList(Property::class.java.classLoader) as ArrayList<Property>
        quantity = parcel.readInt()
        rate = parcel.readFloat()
        if(rate == 0F)
            rate = null
    }

    @Exclude
    fun getImageSlides(): ArrayList<SlideModel> {
        this.imageSlideList.clear()
        for (image in imagesListURI) {
            imageSlideList.add(SlideModel(image.toString()))
        }
        return imageSlideList
    }

    fun calculateNewPrice(): String {
        val value = (this.price - (this.price * (this.discount / 100.0F)))
        this.newPrice = "%.2f".format(value).toFloat()
        return this.newPrice.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productID)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeFloat(price)
        parcel.writeFloat(newPrice)
        parcel.writeInt(discount)
        parcel.writeString(category)
        parcel.writeString(subCategory)
        parcel.writeParcelable(premium, 0)
        parcel.writeStringList(removedImages)
        parcel.writeStringList(images)
        parcel.writeArray(imagesListURI.toArray())
        parcel.writeArray(properties.toArray())
        parcel.writeInt(quantity)
        if(rate == null)
            rate = 0F
        parcel.writeFloat(rate!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}