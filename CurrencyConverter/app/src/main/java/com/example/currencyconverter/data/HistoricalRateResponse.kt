package com.example.currencyconverter.data

import com.google.gson.annotations.SerializedName

data class HistoricalRateResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val motd: Motd,
    val rates: Map<String, Float>,
    val success: Boolean
)

/*
data class Rates(
    @SerializedName(value = "NGN", alternate = ["AMD", "EUR", "AED"])
    val NGN: Float
) {
    override fun toString(): String {
        return "This is the rate $NGN"
    }
}*/
