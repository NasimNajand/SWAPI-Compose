package com.example.swapp.data.species

import com.example.swapp.data.SpecieResponse

interface SpeciesRepository {
    suspend fun getSpeciesById(id: String): SpecieResponse?
}