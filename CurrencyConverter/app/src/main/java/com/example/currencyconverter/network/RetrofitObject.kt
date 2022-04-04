package com.example.currencyconverter.network

import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val baseUrl = "https://v6.exchangerate-api.com/v6/"
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val clientHttp = OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(clientHttp)
        .build()
    fun getService(): ApiService = retrofit.create(ApiService::class.java)

}