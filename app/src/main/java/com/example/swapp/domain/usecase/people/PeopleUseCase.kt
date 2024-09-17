package com.example.swapp.domain.usecase.people

import android.util.Log
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.people.PeopleRepository
import javax.inject.Inject

class PeopleUseCase @Inject constructor(
    private val repository: PeopleRepository
//    private val filmUseCase: FilmUseCase
) {
    companion object {
        private const val TAG = "PeopleUseCase"
    }
    suspend operator fun invoke(page: Int): RemoteResultState<PeopleEntity> {
        Log.d(TAG, "invoke: called")
        return when (val result = repository.getPeople(page)) {
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
                        speciesId = person.species?.firstOrNull()?.trimEnd('/')?.substringAfterLast("/")?:"",
                        planetId = person.homeworld?.trimEnd('/')?.substringAfterLast("/")?:"",
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