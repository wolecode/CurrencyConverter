package com.example.currencyconverter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.data.entity.ConversionResultEntity
import com.example.currencyconverter.data.entity.CurrencyFlagEntity

@Database(entities = [ConversionResultEntity::class, CurrencyFlagEntity::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao

    companion object {
        private var databaseInstance: CurrencyDatabase? = null

        fun getDatabase(context: Context) : CurrencyDatabase {
            return databaseInstance?: synchronized(this) {
                val base = Room.databaseBuilder(context, CurrencyDatabase::class.java,"CurrencyDatabase")
                databaseInstance = base.build()
                databaseInstance!!

            }
        }
    }
}