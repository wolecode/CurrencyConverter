package com.example.currencyconverter.network

import com.example.currencyconverter.data.ConverterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //ffeb91019a6311895383454f/pair/{first}/{second}/{amt}

    @GET("convert")
    suspend fun getConversion(@Query("from")firstCurrency:String,
                              @Query("to")secondCurrency: String,
                              @Query("amount")amt: Float,
                              @Query("round")round: Int = 2):Response<ConverterResponse>
    @GET("{date}")
    suspend fun getHistoricalData(@Path("date") date: String,
                                  @Query("base") base: String,
                                  @Query("symbols") symbols: String,
                                  @Query("amount") amount: Int = 1)
}