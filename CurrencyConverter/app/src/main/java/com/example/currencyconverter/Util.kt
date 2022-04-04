package com.example.currencyconverter

import com.example.currencyconverter.data.entity.CurrencyFlagEntity
import com.example.currencyconverter.data.symbolModels.Symbols


val key = "ffeb91019a6311895383454f"
var newList = mutableListOf<CurrencyFlagEntity>()
val symbolClass: Class<Symbols> = Symbols::class.java
val declaredFields = symbolClass.declaredFields.toList()

fun getCurrencyFlag() : List<CurrencyFlagEntity> {
    for(item in declaredFields) {
        val first = Character.codePointAt(item.name, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(item.name, 1) - 0x41 + 0x1F1E6
        val finalString = CurrencyFlagEntity( item.name, String(Character.toChars(first))
                + String(Character.toChars(second)))
        newList.add(finalString)
    }
    return newList
}