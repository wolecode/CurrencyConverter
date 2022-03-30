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
import com.example.currencyconverter.getCurrencyFlag
import com.example.currencyconverter.network.Results
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConverterViewModel(val app: Application) : AndroidViewModel(app) {

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

    private val databaseDao = CurrencyDatabase.getDatabase(app).getCurrencyDao()

    init {
        loadCurrencyData()
        getConversionResult()

    }

    private fun loadCurrencyData() {
        val pref = app.applicationContext.getSharedPreferences("LOAD_DATA", Context.MODE_PRIVATE)

        if (!pref.getBoolean("isLoaded", false)) {
            viewModelScope.launch {
                databaseDao.insertCurrencySymbol(getCurrencyFlag())
                databaseDao.getListOfCurrencySymbol().collect {
                    _currencySymbol.value = it
                }
                pref.edit().putBoolean("isLoaded", true).apply()
            }
        } else {
            viewModelScope.launch {
                viewModelScope.launch {
                    databaseDao.insertCurrencySymbol(getCurrencyFlag())
                    databaseDao.getListOfCurrencySymbol().collect {
                        _currencySymbol.value = it
                    }
                }
            }
        }
    }

    fun updateFirstCurrency(currency: String) {
        _firstCurrency.value = currency
    }

    fun updateSecondCurrency(currency: String) {
        _secondCurrency.value = currency
    }

    fun convertCurrency(amt: Float, base: String, target: String) {
        viewModelScope.launch {

            val service = RetrofitObject.getService()
            val res = service.getConversion(base, target, amt)

            if (res.isSuccessful) {
                val resBody = res.body()
                _conversion.value =
                    Results.Success(resBody?.conversion_result!!)
                    databaseDao.insertConversionResult(ConversionResultEntity(amt, resBody.base_code,
                    resBody.target_code,resBody.conversion_result))
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
}