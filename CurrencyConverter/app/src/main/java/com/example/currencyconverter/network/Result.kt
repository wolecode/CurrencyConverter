package com.example.currencyconverter.network

sealed class Results<out T> {
    class Success<T>(val data: T) : Results<T>()
    class Error(val error: Throwable): Results<Nothing>()
    object Loading : Results<Nothing>()
}
