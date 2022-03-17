package com.example.currencyconverter

import android.app.Application
import androidx.lifecycle.*
import com.example.currencyconverter.network.Results
import com.example.currencyconverter.network.RetrofitObject
import kotlinx.coroutines.launch

class ConverterViewModel(app: Application) : AndroidViewModel(app){
    private val _firstCurrency = MutableLiveData<String>()
    private val _secondCurrency= MutableLiveData<String>()
    private val _conversion = MutableLiveData<Results<Float>>()

    var firstCurrency :LiveData<String> = _firstCurrency
    var secondCurrency:LiveData<String> = _secondCurrency
    var conversion:LiveData<Results<Float>> = _conversion

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
            if(res.isSuccessful) {
                _conversion.value =
                    Results.Success(res.body()?.conversion_result!!)
            } else {
                Results.Error(Exception(res.errorBody().toString()))
            }
        }
    }
}