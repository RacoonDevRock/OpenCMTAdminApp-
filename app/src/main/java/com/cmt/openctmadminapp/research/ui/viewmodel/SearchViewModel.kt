package com.cmt.openctmadminapp.research.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.research.data.SearchRepository
import com.cmt.openctmadminapp.research.data.network.response.SolicitudDTOResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SolicitudUIState(
    val solicitudes: List<SolicitudDTOResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel() {
    private val _uiState = MutableStateFlow(SolicitudUIState())
    val uiState: StateFlow<SolicitudUIState> = _uiState

    var estadoStr by mutableStateOf<String?>(null)
    var periodoStr by mutableStateOf<String?>(null)

    private var currentPage = 0
    private var isEndReached = false
    private val pageSize = 10

    init {
        loadAllSolicitudes()
    }

    fun loadAllSolicitudes() {
        // Reiniciar la paginación si se inicia una nueva búsqueda
        currentPage = 0
        isEndReached = false
        _uiState.value = _uiState.value.copy(solicitudes = emptyList())  // Limpiar la lista
        loadNextPage()  // Cargar la primera página
    }

    fun refreshSolicitud() {
        // Refrescar incidentes reiniciando el estado
        currentPage = 0
        isEndReached = false
        _uiState.value = _uiState.value.copy(solicitudes = emptyList(), isLoading = true)
        loadNextPage()
    }

    fun loadNextPage() {
        if (isEndReached) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                searchRepository.searchSolicitudes(estadoStr, periodoStr, currentPage, pageSize)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val newSolicitudes = response.body() ?: emptyList()
                    val updatedList = (_uiState.value.solicitudes + newSolicitudes)
                        .distinctBy { it.nroSolicitud }
                    _uiState.value = _uiState.value.copy(
                        solicitudes = updatedList, // Agregar incidentes
                        isLoading = false
                    )
                    currentPage++

                    if (newSolicitudes.size < pageSize) {
                        isEndReached = true
                    }

                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error en la búsqueda",
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error de red",
                    isLoading = false
                )
            }
        }
    }
}