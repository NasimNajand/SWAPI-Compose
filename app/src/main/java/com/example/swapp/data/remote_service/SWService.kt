package com.example.swapp.data.remote_service

import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.Planet
import com.example.swapp.data.RemoteResultState
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SWService {
    @GET("planets/{id}/")
    suspend fun getPlanet(@Path("id") id: Int): Planet

    @GET("people/")
    suspend fun getPeople(@Query("page") page: Int): Response<PeopleResponse>

    @GET("people/")
    suspend fun searchPeople(@Query("search") query: String): Response<PeopleResponse>
}
