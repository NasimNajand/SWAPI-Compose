package com.example.swapp.domain.usecase.species

import com.example.swapp.data.SpeciesEntity
import com.example.swapp.data.species.SpeciesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpeciesUseCase @Inject constructor(
    private val speciesRepository: SpeciesRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(id: String): SpeciesEntity? {
        return withContext(coroutineDispatcher) {
            val result = speciesRepository.getSpeciesById(id)
            if (result != null)
                SpeciesEntity(speciesName = result.name, speciesLang = result.language, homeWorld = result.homeworld)
            else
                null
        }
    }
}