package com.kwekboss.catfacts.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiRequest {

    @GET("/facts/random")

    fun getCatFact(): Call<CatJson>
}