package com.trueandtrust.shoplex.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.model.enumurations.*
import com.trueandtrust.shoplex.model.pojo.Product

class AddProductVM(val context: Context) : ViewModel() {
    var product: MutableLiveData<Product> = MutableLiveData()
    var arrCategory: MutableLiveData<Array<String>> = MutableLiveData()
    var arrSubCategory: MutableLiveData<Array<String>> = MutableLiveData()
    var arrSubCats: Array<String> = arrayOf()

    init {
        product.value = Product()
    }

    fun getCategory(){
        this.arrCategory.value = context.resources.getStringArray(R.array.categories)
            //Category.values().map { it.toString().replace("_", " ") }.toTypedArray()
    }

    fun getSubCategory(selectedCategory: Category){
         val listSubCat =
            when(selectedCategory) {
                Category.Fashion -> {
                    arrSubCats = SubFashion.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subFashion)
                }
                Category.Health_Care -> {
                    arrSubCats = SubHealth.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subHealthCare)
                }
                Category.Phone_and_Tablets -> {
                    arrSubCats = SubPhone.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subPhoneAndTablet)
                }
                Category.Electronics -> {
                    arrSubCats = SubElectronic.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subElectronic)
                }
                Category.Accessories -> {
                    arrSubCats = SubAccessors.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subAccessories)
                }
                Category.Books -> {
                    arrSubCats = SubBook.values().map {
                        it.toString().replace("_", " ")
                    }.toTypedArray()
                    context.resources.getStringArray(R.array.arr_subBook)
                }
            }

        this.arrSubCategory.value = (listSubCat as Array<*>).map {
            it.toString().replace("_", " ")
        }.toTypedArray()
    }
}