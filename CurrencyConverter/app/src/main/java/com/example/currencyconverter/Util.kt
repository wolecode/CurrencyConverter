package com.example.currencyconverter


val key = "ffeb91019a6311895383454f"
val currencyList = listOf("USD","AED","AFN","ALL","AMD","ANG","AOA","ARS",
    "AUD","AWG","AZN","BAM","BBD", "BDT","BGN","BHD","BIF", "BMD","BND","BOB",
    "BRL","BSD","BTN","BWP","BYN","BZD","CAD", "CDF","CHF","CLP","CNY", "COP","CRC",
    "CUP","CVE","CZK","DJF","DKK","DOP","DZD", "EGP","ERN","ETB","EUR", "FJD","FKP","FOK",
    "GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD", "HNL","HRK","HTG", "HUF","IDR",
    "ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KID","KMF",
    "KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LYD","MAD", "MDL","MGA","MKD","MMK",
    "MNT","MOP","MRU","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR",
    "PAB","PEN","PGK","PHP","PKR","PLN","PYG", "QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR",
    "SDG","SEK","SGD","SHP","SLL","SOS","SRD","SSP","STN","SYP", "SZL","THB","TJS","TMT","TND",
    "TOP","TRY","TTD","TVD","TWD","TZS","UAH","UGX","UYU","UZS","VES","VND","VUV","WST","XAF",
    "XCD","XDR","XOF","XPF","YER","ZAR","ZMW","ZWL")
var newList = mutableListOf<String>()

fun getCurrencyFlag() : List<String> {
    for(list in currencyList) {
        val first = Character.codePointAt(list, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(list, 1) - 0x41 + 0x1F1E6
        val finalString = String(Character.toChars(first)) + String(Character.toChars(second)) + " $list"
        newList.add(finalString)
    }
    return newList
}