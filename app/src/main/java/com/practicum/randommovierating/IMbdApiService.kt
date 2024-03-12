package com.practicum.randommovierating

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMbdApiService {

    @GET("/en/API/SearchMovie/$API_KEY/{expression} ")
    fun search(
        @Path("expression") expression: String
    ): Call<MovieResponse>

}