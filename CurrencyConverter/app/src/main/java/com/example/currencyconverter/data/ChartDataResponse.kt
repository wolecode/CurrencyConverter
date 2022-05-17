package com.example.currencyconverter.data

sealed class ChartDataResult <out T> {
    class Success<T>(val result: T) : ChartDataResult<T>()
    class Error(val except: Exception) : ChartDataResult<Nothing>()
    object Loading: ChartDataResult<Nothing>()
}
