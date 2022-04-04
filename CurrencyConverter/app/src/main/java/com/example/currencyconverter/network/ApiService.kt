package com.example.currencyconverter.network

import com.example.currencyconverter.data.ConverterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //ffeb91019a6311895383454f/pair/{first}/{second}/{amt}

    @GET("convert")
    suspend fun getConversion(@Query("from")firstCurrency:String,
                              @Query("to")secondCurrency: String,
                              @Query("amount")amt: Float):Response<ConverterResponse>
}