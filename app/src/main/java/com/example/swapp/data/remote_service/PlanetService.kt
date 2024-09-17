package com.example.swapp.data.remote_service

import com.example.swapp.data.HomeWorldEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlanetService {
    @GET("planets/{id}")
    suspend fun getPlanetData(@Path("id") id: String): Response<HomeWorldEntity>
}