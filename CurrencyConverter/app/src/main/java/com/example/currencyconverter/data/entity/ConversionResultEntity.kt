package com.example.currencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey

@Entity(tableName = "conversionResult")
data class ConversionResultEntity(
                                  @PrimaryKey
                                  val id: Int = 0,
                                  @ColumnInfo
                                  val amount: Float,
                                  @ColumnInfo
                                  val baseCurrency: String,
                                  @ColumnInfo
                                  val targetCurrency: String,
                                  @ColumnInfo
                                  val result: Float) {
}