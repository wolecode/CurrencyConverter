package com.example.currencyconverter.ui

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.currencyconverter.data.CurrencyDao
import com.example.currencyconverter.data.CurrencyDatabase
import com.example.currencyconverter.data.entity.HistoricalDataEntity
import com.example.currencyconverter.network.ApiService
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ConverterWorkManager(private val appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params){

     private lateinit var service: ApiService
     private lateinit var databaseDao: CurrencyDao

    override suspend fun doWork(): Result {
        service = RetrofitObject.getService()
        databaseDao = CurrencyDatabase.getDatabase(appContext).getCurrencyDao()

        val baseCurrency = inputData.getString("BASE_CURRENCY")
        val targetCurrency = inputData.getString("TARGET_CURRENCY")

        val date = LocalDate.now()
        withContext(Dispatchers.IO) {
            for (i in 0..29) {
                val newDate = date.minusDays(i.toLong()).toString()
                val response = service.getHistoricalData(newDate, baseCurrency!!, targetCurrency!!)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        databaseDao.insertHistoricalData(
                            HistoricalDataEntity(
                                newDate,
                                body.rates.values.toList()[0], baseCurrency, targetCurrency
                            )
                        )
                    }
                }
            }
        }
        return Result.success()
    }
}