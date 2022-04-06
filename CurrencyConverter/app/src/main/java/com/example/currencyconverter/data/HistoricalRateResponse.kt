package com.example.currencyconverter.data

data class HistoricalRateResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val motd: Motd,
    val rates: Rates,
    val success: Boolean
)

data class Rates(
    val NGN: Double
)