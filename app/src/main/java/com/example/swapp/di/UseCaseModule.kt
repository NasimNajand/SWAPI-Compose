package com.example.swapp.di

import com.example.swapp.data.film.FilmRepository
import com.example.swapp.data.people.PeopleRepository
import com.example.swapp.data.planet.PlanetRepository
import com.example.swapp.data.search.SearchRepository
import com.example.swapp.data.species.SpeciesRepository
import com.example.swapp.domain.usecase.CharacterDetailUseCase
import com.example.swapp.domain.usecase.convert.MoshiConvertUseCase
import com.example.swapp.domain.usecase.film.FilmUseCase
import com.example.swapp.domain.usecase.people.PeopleUseCase
import com.example.swapp.domain.usecase.planet.PlanetUseCase
import com.example.swapp.domain.usecase.search.SearchPeopleUseCase
import com.example.swapp.domain.usecase.species.SpeciesUseCase
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providePeopleUseCase(repository: PeopleRepository): PeopleUseCase {
        return PeopleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchUseCase(repository: SearchRepository): SearchPeopleUseCase {
        return SearchPeopleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFilmUseCase(repository: FilmRepository): FilmUseCase {
        return FilmUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMoshiConvertUseCase(moshi: Moshi): MoshiConvertUseCase {
        return MoshiConvertUseCase(moshi)
    }

    @Provides
    @Singleton
    fun provideSpeciesUseCase(speciesRepository: SpeciesRepository): SpeciesUseCase {
        return SpeciesUseCase(speciesRepository)
    }

    @Provides
    @Singleton
    fun providePlanetUseCase(planetRepository: PlanetRepository): PlanetUseCase {
        return PlanetUseCase(planetRepository)
    }

    @Provides
    @Singleton
    fun provideCharacterDetailUseCase(repository: SearchRepository,
                                      filmUseCase: FilmUseCase,
                                      speciesUseCase: SpeciesUseCase,
                                      planetUseCase: PlanetUseCase): CharacterDetailUseCase {
        return CharacterDetailUseCase(repository, filmUseCase, speciesUseCase, planetUseCase)
    }

}