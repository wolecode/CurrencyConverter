package com.example.currencyconverter.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Currency
import android.util.Log
import androidx.lifecycle.*
import com.example.currencyconverter.data.CurrencyDatabase
import com.example.currencyconverter.data.entity.CurrencyFlagEntity
import com.example.currencyconverter.getCurrencyFlag
import com.example.currencyconverter.network.Results
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.launch

class ConverterViewModel(val app: Application) : AndroidViewModel(app) {
    private val _firstCurrency = MutableLiveData<String>()
    private val _secondCurrency = MutableLiveData<String>()
    private val _conversion = MutableLiveData<Results<Float>>()
    private var _currencySymbol = MutableLiveData<List<CurrencyFlagEntity>>()

    var firstCurrency: LiveData<String> = _firstCurrency
    var secondCurrency: LiveData<String> = _secondCurrency
    var conversion: LiveData<Results<Float>> = _conversion
    var currencySymbol: LiveData<List<CurrencyFlagEntity>> = _currencySymbol


    private val databaseDao = CurrencyDatabase.getDatabase(app).getCurrencyDao()

    init {
        loadCurrencyData()

    }

    private fun loadCurrencyData() {
        val pref = app.applicationContext.getSharedPreferences("LOAD_DATA", Context.MODE_PRIVATE)

        if (!pref.getBoolean("isLoaded", false)) {
            viewModelScope.launch {
                databaseDao.insertCurrencySymbol(getCurrencyFlag())
                currencySymbol = databaseDao.getListOfCurrencySymbol()
                pref.edit().putBoolean("isLoaded", true).apply()
            }
        } else {
            viewModelScope.launch {
                currencySymbol = databaseDao.getListOfCurrencySymbol().distinctUntilChanged()

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
                _conversion.value =
                    Results.Success(res.body()?.conversion_result!!)
            } else {
                Results.Error(Exception(res.errorBody().toString()))
            }
        }
    }
}