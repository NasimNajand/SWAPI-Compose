package com.example.swapp.di

import com.example.swapp.data.film.FilmRepository
import com.example.swapp.data.film.FilmRepositoryImpl
import com.example.swapp.data.people.PeopleRepository
import com.example.swapp.data.people.PeopleRepositoryImpl
import com.example.swapp.data.planet.PlanetRepository
import com.example.swapp.data.planet.PlanetRepositoryImpl
import com.example.swapp.data.remote_service.FilmService
import com.example.swapp.data.remote_service.PlanetService
import com.example.swapp.data.remote_service.SWService
import com.example.swapp.data.remote_service.SpeciesService
import com.example.swapp.data.search.SearchRepository
import com.example.swapp.data.search.SearchRepositoryImpl
import com.example.swapp.data.species.SpeciesRepository
import com.example.swapp.data.species.SpeciesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePeopleRepository(
        api: SWService
    ): PeopleRepository = PeopleRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSearchRepository(
        api: SWService
    ): SearchRepository = SearchRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideFilmRepository(
        service: FilmService
    ): FilmRepository = FilmRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideSpeciesRepository(
        service: SpeciesService
    ): SpeciesRepository = SpeciesRepositoryImpl(service)

    @Provides
    @Singleton
    fun providePlanetRepository(
        planetService: PlanetService
    ): PlanetRepository = PlanetRepositoryImpl(planetService)
}