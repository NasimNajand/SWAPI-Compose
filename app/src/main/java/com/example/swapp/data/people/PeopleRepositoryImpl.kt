package com.example.swapp.data.people

import android.util.Log
import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.remote_service.SWService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.checkOffsetAndCount
import retrofit2.HttpException
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val service: SWService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): PeopleRepository {
    companion object {
        private const val TAG = "PeopleRepositoryImpl"
    }
    override suspend fun getPeople(page: Int): RemoteResultState<PeopleResponse> {
        Log.d(TAG, "getPeople: API call initiated for page: $page")

        return withContext(coroutineDispatcher) {
            runCatching {
                val response = service.getPeople(page)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        Log.d(TAG, "getPeople: success with data: $it")
                        RemoteResultState.Success(it)
                    } ?: run {
                        Log.e(TAG, "getPeople: response body is null")
                        RemoteResultState.Error(IllegalStateException("Response body is null"))
                    }
                } else {
                    Log.e(TAG, "getPeople: API error with code: ${response.code()}")
                    RemoteResultState.Error(HttpException(response))
                }
            }.getOrElse { exception ->
                Log.e(TAG, "getPeople: Exception occurred: ${exception.localizedMessage}", exception)
                RemoteResultState.Error(exception)
            }
        }
    }

}