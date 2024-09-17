package com.example.swapp.ui

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import com.example.swapp.domain.usecase.people.PeopleUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class PeoplePagingSource(
    private val peopleUseCase: PeopleUseCase,
    private val loadingState: MutableStateFlow<Boolean>
) : PagingSource<Int, CharacterEntity>() {

    companion object {
        private const val TAG = "PeoplePagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val page = params.key ?: 1

        return try {
            loadingState.value = true
            when (val result = peopleUseCase.invoke(page)) {
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
            loadingState.value = false
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
