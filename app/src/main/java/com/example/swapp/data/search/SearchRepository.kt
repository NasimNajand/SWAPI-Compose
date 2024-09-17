package com.example.swapp.data.search

import androidx.paging.PagingData
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchPeople(query: String): RemoteResultState<PeopleResponse>
}