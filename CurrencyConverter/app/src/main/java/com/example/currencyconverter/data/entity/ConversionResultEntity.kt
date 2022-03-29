package com.example.currencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "conversionResult")
data class ConversionResultEntity(
                                  @PrimaryKey
                                  val id: Int,
                                  @ColumnInfo
                                  val amount: Float,
                                  @ColumnInfo
                                  val baseCurrency: String,
                                  @ColumnInfo
                                  val targetCurrency: String,
                                  @ColumnInfo
                                  val result: Float) {

    constructor(amount: Float, baseCurrency: String, targetCurrency: String, result: Float)
            : this(0, amount, baseCurrency, targetCurrency, result)
}