package com.example.currencyconverter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyconverter.data.entity.ConversionResultEntity
import com.example.currencyconverter.data.entity.CurrencyFlagEntity
import com.example.currencyconverter.getCurrencyFlag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(entities = [ConversionResultEntity::class, CurrencyFlagEntity::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao

    companion object {
        private var databaseInstance: CurrencyDatabase? = null
        private val scope = CoroutineScope(Job() + Dispatchers.IO)

        fun getDatabase(context: Context) : CurrencyDatabase {
            return databaseInstance?: synchronized(this) {
                val base = Room.databaseBuilder(context, CurrencyDatabase::class.java,"CurrencyDatabase")
                    .addCallback(object: RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            loadData()
                        }
                    })
                databaseInstance = base.build()
                databaseInstance!!

            }
        }

        fun loadData() {
         scope.launch {
             databaseInstance?.getCurrencyDao()?.insertCurrencySymbol(getCurrencyFlag())
         }
        }
    }
}