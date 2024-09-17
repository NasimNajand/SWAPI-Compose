package com.example.swapp.data.planet

import com.example.swapp.data.HomeWorldEntity
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.remote_service.PlanetService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanetRepositoryImpl @Inject constructor(
    private val planetService: PlanetService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlanetRepository {
    override suspend fun getHomeWorldData(id: String): RemoteResultState<HomeWorldEntity> {
        return withContext(coroutineDispatcher) {
            val response = planetService.getPlanetData(id)
            if (response.isSuccessful && response.body() != null)
                return@withContext response.body()
                    ?.let { RemoteResultState.Success(response.body()!!) } ?: run { RemoteResultState.Error(Exception("Null Response")) }
            else
                return@withContext RemoteResultState.Error(
                    Exception(
                        response.errorBody().toString()
                    )
                )
        }
    }

}