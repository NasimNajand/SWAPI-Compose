package com.example.swapp.domain.usecase.film

import android.util.Log
import com.example.swapp.data.FilmResponse
import com.example.swapp.data.film.FilmRepository
import com.example.swapp.data.remote_service.FilmService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilmUseCase @Inject constructor(private val filmRepository: FilmRepository)  {
    companion object {
        private const val TAG = "FilmUseCase"
    }
    suspend operator fun invoke(filmIds: List<String>): List<FilmResponse> {
        val filmDeferred = withContext(Dispatchers.IO) {
            filmIds.map { id ->
                async {
                    val film = filmRepository.getFilm(id)
                    Log.d(TAG, "film title: ${film?.title}")
                    film
                }
            }
        }
        return filmDeferred.awaitAll().filterNotNull()
    }
}