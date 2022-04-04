package com.example.currencyconverter.data

data class ConverterResponse(
    val date: String,
    val historical: Boolean,
    val info: Info,
    val motd: Motd,
    val query: Query,
    val result: Float,
    val success: Boolean
)

data class Info(
    val rate: Double
)

data class Motd(
    val msg: String,
    val url: String
)

data class Query(
    val amount: Float,
    val from: String,
    val to: String
)