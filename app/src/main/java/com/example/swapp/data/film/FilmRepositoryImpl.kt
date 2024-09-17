package com.example.swapp.data.film

import android.util.Log
import com.example.swapp.data.FilmResponse
import com.example.swapp.data.remote_service.FilmService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val filmService: FilmService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): FilmRepository {
    companion object {
        private const val TAG = "FilmRepositoryImpl"
    }

    override suspend fun getFilm(id: String): FilmResponse? {
        Log.d(TAG, "getFilm: called with id : $id")
        return withContext(dispatcher) {
            val result = filmService.getFilm(id)
            if (result.isSuccessful && result.body() != null)
                return@withContext result.body()
            else
                return@withContext null
        }
    }
}