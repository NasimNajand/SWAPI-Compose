package com.example.swapp.data.film


import com.example.swapp.data.FilmResponse

interface FilmRepository {
    suspend fun getFilm(id: String): FilmResponse?
}