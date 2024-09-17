package com.example.swapp.data.remote_service


import com.example.swapp.data.SpecieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpeciesService {
    @GET("species/{id}")
    suspend fun getSpeciesData(@Path("id") id: String): Response<SpecieResponse>
}