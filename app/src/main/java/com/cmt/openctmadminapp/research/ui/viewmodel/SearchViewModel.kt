package com.cmt.openctmadminapp.research.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.cmt.openctmadminapp.research.data.RequestPagingResource
import com.cmt.openctmadminapp.research.data.SearchRepository
import com.cmt.openctmadminapp.research.data.network.response.Filters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel() {
    var estadoStr by mutableStateOf<String?>(null)
    var periodoStr by mutableStateOf<String?>(null)

    private val filters = MutableStateFlow(Filters())

    @OptIn(ExperimentalCoroutinesApi::class)
    val requestsFlow = filters.flatMapLatest { filter ->
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RequestPagingResource(
                    repository = searchRepository,
                    estadoStr = filter.estadoStr,
                    periodoStr = filter.periodoStr
                )
            }
        ).flow
    }.cachedIn(viewModelScope)

    fun applyFilters(estadoStr: String?, periodoStr: String?) {
        filters.value = Filters(estadoStr, periodoStr)
    }

    fun loadAllSolicitudes() {
        filters.value = Filters() // Sin filtros
    }

    fun refreshData() {
        fun refreshData() {
            viewModelScope.launch {
                loadAllSolicitudes()
            }
        }
    }
}