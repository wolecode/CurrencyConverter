package com.example.currencyconverter.ui

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.currencyconverter.data.CurrencyDao
import com.example.currencyconverter.data.CurrencyDatabase
import com.example.currencyconverter.network.ApiService
import com.example.currencyconverter.network.RetrofitObject

class ConverterWorkManager(private val appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params){
     private lateinit var service: ApiService
     private lateinit var databaseDao: CurrencyDao

    override suspend fun doWork(): Result {
        service = RetrofitObject.getService()
        databaseDao = CurrencyDatabase.getDatabase(appContext).getCurrencyDao()
        inputData.getString()
        TODO("Not yet implemented")
    }
}