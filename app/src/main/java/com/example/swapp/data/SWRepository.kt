package com.example.swapp.data

import com.example.swapp.data.remote_service.SWService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SWRepository @Inject constructor(
    private val service: SWService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getAllPlanetsWithResidents(): List<Planet> = withContext(dispatcher) {
        val planetRequests = (1..60).map { id ->
            async {
                try {
                    val planet = service.getPlanet(id)
                    if (planet.residents.isNotEmpty()) {
                        planet
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
        planetRequests.awaitAll().filterNotNull()
    }
}