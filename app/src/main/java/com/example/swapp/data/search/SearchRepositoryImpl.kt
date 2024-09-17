package com.example.swapp.data.search

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.people.PeopleRepositoryImpl
import com.example.swapp.data.remote_service.SWService
import com.example.swapp.ui.SearchPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val service: SWService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRepository {
    companion object {
        private const val TAG = "SearchRepositoryImpl"
    }

    override suspend fun searchPeople(query: String): RemoteResultState<PeopleResponse> {
        return withContext(coroutineDispatcher) {
            runCatching {
                val response = service.searchPeople(query)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        Log.d(TAG, "searchPeople: success with data: $it")
                        RemoteResultState.Success(it)
                    } ?: run {
                        Log.e(TAG, "searchPeople: response body is null")
                        RemoteResultState.Error(IllegalStateException("Response body is null"))
                    }
                } else {
                    Log.e(TAG, "searchPeople: API error with code: ${response.code()}")
                    RemoteResultState.Error(HttpException(response))
                }
            }.getOrElse { exception ->
                Log.e(TAG, "searchPeople: Exception occurred: ${exception.localizedMessage}", exception)
                RemoteResultState.Error(exception)
            }
        }
    }
}