package com.cmt.openctmadminapp.report.incidentToDetail.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.report.incidentToDetail.data.DetailIncidentRepository
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.IncidenteDTODetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailIncidentViewModel @Inject constructor(private val detailIncidentRepository: DetailIncidentRepository) :
    ViewModel() {

    private val _incidentDetail = MutableStateFlow<IncidenteDTODetail?>(null)
    val incidentDetail: StateFlow<IncidenteDTODetail?> = _incidentDetail

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadIncidentDetail(nroSolicitud: String) {
        _isLoading.value = true
        viewModelScope.launch {
            runCatching {
                detailIncidentRepository.getIncidenteDetail(nroSolicitud)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _incidentDetail.value = response.body()
                    } else {
                        Log.d("DetailIncidentViewModel", "Error code: ${response.code()}")
                    }
            }.onFailure {
                    Log.d("DetailIncidentViewModel", "Network error: ${it.localizedMessage}")
            }
            _isLoading.value = false
        }
    }
}