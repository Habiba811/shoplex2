package com.trueandtrust.shoplex.room.data

import android.content.Context
import androidx.room.*
import com.trueandtrust.shoplex.model.pojo.Message
import com.trueandtrust.shoplex.model.pojo.Order
import com.trueandtrust.shoplex.model.pojo.Product

@Database(entities = [Message::class, Product::class,Order::class], version = 1)
@TypeConverters(Converters::class)
abstract class ShopLexDatabase : RoomDatabase() {

    abstract fun storeDao() : StoreDao

    companion object{
        @Volatile
        private var INSTANCE : ShopLexDatabase? = null
        fun getDatabase(context: Context):ShopLexDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShopLexDatabase::class.java,
                    "Shoplex_Store_Database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}