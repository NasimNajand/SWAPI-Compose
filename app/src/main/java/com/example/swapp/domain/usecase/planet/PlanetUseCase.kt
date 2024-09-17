package com.example.swapp.domain.usecase.planet

import android.util.Log
import com.example.swapp.data.HomeWorldEntity
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.planet.PlanetRepository
import javax.inject.Inject

class PlanetUseCase @Inject constructor(private val planetRepository: PlanetRepository) {
    companion object {
        private const val TAG = "PlanetUseCase"
    }
    suspend operator fun invoke(id: String): HomeWorldEntity? {
        Log.d(TAG, "invoke: caleld for $id")
        return when (val response = planetRepository.getHomeWorldData(id)) {
            is RemoteResultState.Success -> response.data
            else -> null
        }
    }
}