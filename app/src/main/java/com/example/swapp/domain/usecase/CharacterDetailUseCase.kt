package com.example.swapp.domain.usecase

import android.util.Log
import com.example.swapp.data.CharacterDetail
import com.example.swapp.data.CharacterDetailHolder
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.RemoteResultState
import com.example.swapp.data.search.SearchRepository
import com.example.swapp.domain.usecase.film.FilmUseCase
import com.example.swapp.domain.usecase.planet.PlanetUseCase
import com.example.swapp.domain.usecase.species.SpeciesUseCase

class CharacterDetailUseCase(
    private val repository: SearchRepository,
    private val filmUseCase: FilmUseCase,
    private val speciesUseCase: SpeciesUseCase,
    private val planetUseCase: PlanetUseCase
) {

    companion object {
        private const val TAG = "CharacterDetailUseCase"
    }

    suspend operator fun invoke(name: String): RemoteResultState<CharacterDetailHolder> {
        return when (val result = repository.searchPeople(name)) {
            is RemoteResultState.Loading -> {
                RemoteResultState.Loading
            }

            is RemoteResultState.Success -> {

                val list = mutableListOf<CharacterDetail>()
                val peopleEntity = CharacterDetailHolder(
                    count = result.data.count,
                    next = result.data.next,
                    previous = result.data.previous,
                    list
                )
                result.data.results.map { person ->
                    list.add(
                        CharacterDetail(
                            speciesId = person.species?.firstOrNull()?.trimEnd('/')
                                ?.substringAfterLast("/"),
                            planetId = person.homeworld?.trimEnd('/')?.substringAfterLast("/"),
                            filmsIds = person.films?.map { url ->
                                url.trimEnd('/').substringAfterLast("/")
                            },
                            name = person.name,
                            birthYear = person.birthYear,
                            height = person.height
                        )
                    )
                }
                list.firstOrNull()?.let { character ->
                    val filmResult = character.filmsIds?.let { filmUseCase.invoke(it) }
                    filmResult?.map { film ->
                        character.filmMap[film.title?:""] = film.openingCrawl?:""
                    }
                    val speciesId = character.speciesId
                    if (!speciesId.isNullOrEmpty()) {
                        val result = speciesUseCase.invoke(speciesId)
                        character.speciesName = result?.speciesName?:""
                        character.speciesLang = result?.speciesLang?:""
                    }

                    Log.d(TAG, "invoke: home world : ${character.homeWorldName}")
                    if (!character.homeWorldName.isNullOrEmpty()) {
                        // get homeWorld data
                        character.homeWorldName?.trimEnd('/')?.substringAfterLast("/")?.let { planetId ->
                            Log.d(TAG, "invoke: homeWorldName -> $planetId")
                            planetUseCase.invoke(planetId)?.let { planet ->
                                Log.d(TAG, "invoke: -> name , terrain : ${planet.name} : ${planet.terrain}")
                                character.homeWorldName = planet.name
                                character.homeWorldTerrain = planet.terrain
                            }
                        }
                    }
                }
                RemoteResultState.Success(peopleEntity)
            }

            is RemoteResultState.Error -> {
                RemoteResultState.Error(result.exception)
            }
        }
    }
}