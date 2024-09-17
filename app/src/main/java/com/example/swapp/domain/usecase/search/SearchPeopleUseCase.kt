package com.example.swapp.domain.usecase.search

import android.util.Log
import androidx.paging.PagingData
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.search.SearchRepository
import com.example.swapp.domain.usecase.people.PeopleUseCase
import kotlinx.coroutines.flow.Flow

class SearchPeopleUseCase(private val repository: SearchRepository) {
    companion object {
        private const val TAG = "SearchPeopleUseCase"
    }
    suspend operator fun invoke(query: String): RemoteResultState<PeopleEntity> {
        Log.d(TAG, "invoke: called")
        return when (val result = repository.searchPeople(query)) {
            is RemoteResultState.Loading -> {
                RemoteResultState.Loading
            }
            is RemoteResultState.Success -> {

                val list = mutableListOf<CharacterEntity>()
                val peopleEntity = PeopleEntity(
                    count = result.data.count,
                    next = result.data.next,
                    previous = result.data.previous,
                    list
                )
                result.data.results.map { person ->
                    list.add(CharacterEntity(
                        speciesId = person.species?.firstOrNull()?.trimEnd('/')?.substringAfterLast("/"),
                        planetId = person.homeworld?.trimEnd('/')?.substringAfterLast("/"),
                        filmsIds = person.films?.map { url ->url.trimEnd('/').substringAfterLast("/") },
                        name = person.name,
                        birthYear = person.birthYear,
                        height = person.height
                    ))
                }
                RemoteResultState.Success(peopleEntity)
            }
            is RemoteResultState.Error -> {
                RemoteResultState.Error(result.exception)
            }
        }
    }
}