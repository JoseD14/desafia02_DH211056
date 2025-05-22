package com.example.desafio02

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://68163b7232debfe95dbdd500.mockapi.io/academic/v1/"

    val api: CalendarioApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(CalendarioApi::class.java)


    }
}