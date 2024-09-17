package com.example.swapp.data.people

import com.example.swapp.data.PeopleResponse
import com.example.swapp.data.RemoteResultState

interface PeopleRepository {
    suspend fun getPeople(page: Int): RemoteResultState<PeopleResponse>
}