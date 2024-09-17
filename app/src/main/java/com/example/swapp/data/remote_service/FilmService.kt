package com.example.swapp.data.remote_service

import com.example.swapp.data.FilmResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmService {
    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: String): Response<FilmResponse>
}