package com.example.desafio02

import retrofit2.Call
import retrofit2.http.GET

interface CalendarioApi{
    @GET("events")
    fun getEvents(): Call<List<Event>>
}