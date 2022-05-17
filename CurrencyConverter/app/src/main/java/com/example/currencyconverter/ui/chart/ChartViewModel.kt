package com.example.currencyconverter.ui.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.CurrencyDatabase
import com.example.currencyconverter.data.entity.HistoricalDataEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChartViewModel(app: Application): AndroidViewModel(app) {

    private val databaseDao = CurrencyDatabase.getDatabase(app).getCurrencyDao()
    lateinit var historicalData : List<HistoricalDataEntity>

    init {
        getHistoricalData()
    }

    private fun getHistoricalData() {
        viewModelScope.launch {
            databaseDao.getHistoricalData().collect {
                historicalData = it
            }
        }
    }
}