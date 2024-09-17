package com.example.swapp.di

import com.example.swapp.data.CustomInterceptor
import com.example.swapp.data.remote_service.FilmService
import com.example.swapp.data.remote_service.PlanetService
import com.example.swapp.data.remote_service.SWService
import com.example.swapp.data.remote_service.SpeciesService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://swapi.dev/api/"

    @Singleton
    @Provides
    fun provideCustomInterceptor(): CustomInterceptor {
        return CustomInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(customInterceptor: CustomInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(customInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideSWApi(retrofit: Retrofit): SWService = retrofit.create(SWService::class.java)

    @Provides
    @Singleton
    fun provideFilmService(retrofit: Retrofit): FilmService = retrofit.create(FilmService::class.java)

    @Provides
    @Singleton
    fun provideSpeciesService(retrofit: Retrofit): SpeciesService = retrofit.create(SpeciesService::class.java)

    @Provides
    @Singleton
    fun providePlanetService(retrofit: Retrofit): PlanetService = retrofit.create(PlanetService::class.java)
}