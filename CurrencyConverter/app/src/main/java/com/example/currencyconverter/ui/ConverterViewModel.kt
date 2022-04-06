package com.example.currencyconverter.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Currency
import android.util.Log
import androidx.lifecycle.*
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

    init {
        loadCurrencyData()
        getConversionResult()
        getHistoricalDataSample()
    }
    private fun getHistoricalDataSample() {
        viewModelScope.launch {
            historicalSample = databaseDao.getHistoricalDataSample()
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
                insertHistoricalDataLocally(base, target)
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

    private fun insertHistoricalDataLocally(baseCurrency: String, targetCurrency: String) {
        if (historicalSample == null) {
            val date = LocalDate.now()
            viewModelScope.launch {

                    async {
                        for( i in 0..16) {
                            val newDate = date.minusDays(i.toLong()).toString()
                            val response = service.getHistoricalData(newDate, baseCurrency, targetCurrency)
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null) {
                                    databaseDao.insertHistoricalData(
                                        HistoricalDataEntity(newDate,
                                            body.rates.values.toList()[0], baseCurrency, targetCurrency)
                                    )
                                }
                            }
                        }
                    }
                    async {
                        for(i in 16..30) {
                            val newDate = date.minusDays(i.toLong()).toString()
                            val response = service.getHistoricalData(newDate, baseCurrency, targetCurrency)
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null) {
                                    databaseDao.insertHistoricalData(
                                        HistoricalDataEntity(newDate,
                                            body.rates.values.toList()[0], baseCurrency, targetCurrency)
                                    )
                                }
                            }
                        }
                    }

            }
        } else {
            val date = LocalDate.now()
            viewModelScope.launch {
                val response = service.getHistoricalData(date.toString(), baseCurrency, targetCurrency)
                Log.i("RATE","${response.body()?.rates?.values?.toList()?.get(0)}")

            }

        }

    }
}