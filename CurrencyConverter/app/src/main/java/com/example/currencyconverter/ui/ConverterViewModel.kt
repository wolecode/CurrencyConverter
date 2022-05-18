package com.example.currencyconverter.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Currency
import android.util.Log
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.example.currencyconverter.data.CurrencyDatabase
import com.example.currencyconverter.data.entity.ConversionResultEntity
import com.example.currencyconverter.data.entity.CurrencyFlagEntity
import com.example.currencyconverter.data.entity.HistoricalDataEntity
import com.example.currencyconverter.getCurrencyFlag
import com.example.currencyconverter.network.Results
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

class ConverterViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _firstCurrency = MutableLiveData<String>()
    private val _secondCurrency = MutableLiveData<String>()
    private val _conversion = MutableLiveData<Results<Float>>(Results.Loading)
    private var _currencySymbol = MutableLiveData<List<CurrencyFlagEntity>>()

    private val _conversionResult = MutableLiveData<List<ConversionResultEntity>>()

    var firstCurrency: LiveData<String> = _firstCurrency
    var secondCurrency: LiveData<String> = _secondCurrency
    var conversion: LiveData<Results<Float>> = _conversion
    var currencySymbol: LiveData<List<CurrencyFlagEntity>> = _currencySymbol
    var conversionResult: LiveData<List<ConversionResultEntity>> = _conversionResult

    var historicalSample: HistoricalDataEntity? = null
    private val service = RetrofitObject.getService()

    private val databaseDao = CurrencyDatabase.getDatabase(app).getCurrencyDao()
    private val workManger = WorkManager.getInstance(app)

    init {
        loadCurrencyData()
        getConversionResult()
        getHistoricalDataSample()
    }

    private fun getHistoricalDataSample() {
        viewModelScope.launch {
            databaseDao.getHistoricalDataSample().collect {
                historicalSample = it
            }
        }

    }

    private fun loadCurrencyData() {
        viewModelScope.launch {
            databaseDao.getListOfCurrencySymbol().collect {
                _currencySymbol.value = it
            }
        }
    }

    fun updateFirstCurrency(currency: String) {
        _firstCurrency.value = currency
    }

    fun updateSecondCurrency(currency: String) {
        _secondCurrency.value = currency
    }

    fun convertCurrency(
        amt: Float, base: String, target: String,
        basePosition: String, targetPosition: String
    ) {
        viewModelScope.launch {

            val res = service.getConversion(base, target, amt)

            if (res.isSuccessful) {
                val resBody = res.body()
                _conversion.value =
                    Results.Success(resBody?.result!!)
                databaseDao.insertConversionResult(
                    ConversionResultEntity(
                        amt, basePosition,
                        targetPosition, resBody.result
                    )
                )
                // Fetch and save historical data for the last thirty(30) days
                updateHistoricalDataLocally(base, target)
            } else {
                Results.Error(Exception(res.errorBody().toString()))
            }
        }
    }

    private fun getConversionResult() {
        viewModelScope.launch {
            databaseDao.getConversionResult().collect {
                _conversionResult.value = it
            }
        }

    }

    private fun updateHistoricalDataLocally(baseCurrency: String, targetCurrency: String) {
        val date = LocalDate.now()
        if (historicalSample == null) {
           insertHistoricalDataLocally(baseCurrency, targetCurrency)
        } else if(historicalSample!!.baseCurrency != baseCurrency ||
            historicalSample!!.targetCurrency != targetCurrency ||
            historicalSample!!.date != date.toString()){

            viewModelScope.launch {
                databaseDao.deleteHistoricalData()
                insertHistoricalDataLocally(baseCurrency, targetCurrency)
            }

        }
    }

    private fun insertHistoricalDataLocally(baseCurrency: String, targetCurrency: String) {
        val date = LocalDate.now()
        viewModelScope.launch {
            for (i in 0..29) {
                val newDate = date.minusDays(i.toLong()).toString()
                val response = service.getHistoricalData(newDate, baseCurrency, targetCurrency)
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
    }
}