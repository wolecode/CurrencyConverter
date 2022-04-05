package com.example.currencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName = "historicalData")
data class HistoricalDataEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val rate: Float,
    @ColumnInfo
    val baseCurrency: String,
    @ColumnInfo
    val targetCurrency: String) {
    constructor(date: String, rate: Float, baseCurrency: String, targetCurrency: String ): this(UUID.randomUUID().toString(),
    date, rate, baseCurrency, targetCurrency)
}
