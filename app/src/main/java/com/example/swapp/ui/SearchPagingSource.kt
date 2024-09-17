package com.example.swapp.ui

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.remote_service.SWService
import com.example.swapp.domain.usecase.search.SearchPeopleUseCase

class SearchPagingSource(
    private val searchPeopleUseCase: SearchPeopleUseCase,
    private val query: String
) : PagingSource<Int, CharacterEntity>() {

    companion object {
        private const val TAG = "SearchPagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val page = params.key ?: 1

        return try {
            when (val result = searchPeopleUseCase.invoke(query)) {
                is RemoteResultState.Success -> {
                    Log.d(TAG, "load: success")
                    LoadResult.Page(
                        data = result.data.characters,
                        prevKey = null,
                        nextKey = if (result.data.next != null) page + 1 else null
                    )
                }
                is RemoteResultState.Error -> {
                    Log.e(TAG, "load: error ", result.exception)
                    LoadResult.Error(result.exception ?: Throwable("Unknown error"))
                }

                is RemoteResultState.Loading -> {
                    Log.d(TAG, "load: loading")
                    LoadResult.Error(Throwable())
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        } finally {
            Log.d(TAG, "load: finally")
//            loadingState.value = false
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}
