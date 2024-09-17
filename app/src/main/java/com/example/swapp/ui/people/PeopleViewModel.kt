package com.example.swapp.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.swapp.data.CharacterDetailHolder
import com.example.swapp.data.CharacterEntity
import com.example.swapp.data.PeopleEntity
import com.example.swapp.data.Person
import com.example.swapp.data.RemoteResultState
import com.example.swapp.domain.usecase.CharacterDetailUseCase
import com.example.swapp.domain.usecase.convert.MoshiConvertUseCase
import com.example.swapp.ui.PeoplePagingSource
import com.example.swapp.domain.usecase.people.PeopleUseCase
import com.example.swapp.domain.usecase.search.SearchPeopleUseCase
import com.example.swapp.ui.SearchPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getPeopleUseCase: PeopleUseCase,
    private val searchPeopleUseCase: SearchPeopleUseCase,
    private val moshiConvertUseCase: MoshiConvertUseCase,
    private val characterDetailUseCase: CharacterDetailUseCase
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _peoplePagingData = MutableStateFlow<Flow<PagingData<CharacterEntity>>>(
        Pager(PagingConfig(pageSize = 10)) {
            PeoplePagingSource(getPeopleUseCase, _loadingState)
        }.flow.cachedIn(viewModelScope)
    )
    val peoplePagingData: StateFlow<Flow<PagingData<CharacterEntity>>> = _peoplePagingData

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isEmpty()) {
                        _peoplePagingData.value = Pager(PagingConfig(pageSize = 10)) {
                            PeoplePagingSource(getPeopleUseCase, _loadingState)
                        }.flow.cachedIn(viewModelScope)
                    } else {
                        _peoplePagingData.value = Pager(PagingConfig(pageSize = 10)) {
                            SearchPagingSource(searchPeopleUseCase, query)
                        }.flow.cachedIn(viewModelScope)
                    }
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _apiResult = MutableStateFlow<RemoteResultState<CharacterDetailHolder?>>(RemoteResultState.Loading)
    val apiResult: StateFlow<RemoteResultState<CharacterDetailHolder?>> = _apiResult
    fun searchByName(name: String) {
        viewModelScope.launch {
            _apiResult.value = characterDetailUseCase.invoke(name)
        }
    }
}
