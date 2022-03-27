package com.example.currencyconverter.network

import com.example.currencyconverter.data.ConverterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("ffeb91019a6311895383454f/pair/{first}/{second}/{amt}")
    suspend fun getConversion(@Path("first")firstCurrency:String,
                              @Path("second")secondCurrency: String,
                              @Path("amt")amt: Float):Response<ConverterResponse>
}