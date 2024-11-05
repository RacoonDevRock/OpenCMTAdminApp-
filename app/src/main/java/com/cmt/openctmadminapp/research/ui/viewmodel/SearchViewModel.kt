package com.cmt.openctmadminapp.research.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.research.ui.data.SearchRepository
import com.cmt.openctmadminapp.research.ui.data.network.response.SolicitudDTOResponse
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

    init {
        loadAllSolicitudes()
    }

    fun loadAllSolicitudes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            runCatching {
                searchRepository.searchSolicitudes(estadoStr, periodoStr)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _uiState.value = SolicitudUIState(response.body() ?: emptyList())
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Error en la búsqueda")
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(errorMessage = "Error de red")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}