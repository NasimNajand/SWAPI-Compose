package com.example.swapp.data.species

import com.example.swapp.data.SpecieResponse
import com.example.swapp.data.remote_service.SpeciesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpeciesRepositoryImpl @Inject constructor(
    private val service: SpeciesService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): SpeciesRepository {
    override suspend fun getSpeciesById(id: String): SpecieResponse? {
        return withContext(dispatcher) {
            val result = service.getSpeciesData(id)
            if (result.isSuccessful && result.body() != null)
                result.body()
            else
                null
        }
    }
}