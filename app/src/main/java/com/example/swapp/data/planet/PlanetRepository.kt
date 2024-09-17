package com.example.swapp.data.planet

import com.example.swapp.data.HomeWorldEntity
import com.example.swapp.data.RemoteResultState

interface PlanetRepository {
    suspend fun getHomeWorldData(id: String): RemoteResultState<HomeWorldEntity>
}