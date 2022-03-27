package com.example.currencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "currencyFlag")
data class CurrencyFlagEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val currency: String ,
    @ColumnInfo
    val flagSymbol: String
){
constructor(currency: String, flag: String): this(UUID.randomUUID().toString(), currency, flag)
}
