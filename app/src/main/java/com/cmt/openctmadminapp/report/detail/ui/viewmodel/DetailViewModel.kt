package com.cmt.openctmadminapp.report.detail.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.report.detail.data.DetailRepository
import com.cmt.openctmadminapp.report.detail.data.network.response.SolicitudDTODetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    private val _solicitudDetail = MutableStateFlow<SolicitudDTODetail?>(null)
    val solicitudDetail: StateFlow<SolicitudDTODetail?> = _solicitudDetail

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadSolicitudDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                detailRepository.getSolicitudDetail(id)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    _solicitudDetail.value = response.body()
                } else {
                    Log.d("DetailViewModel", "Error code: ${response.code()}")
                }
            }.onFailure {
                Log.d("DetailViewModel", "Network error: ${it.localizedMessage}")
            }
            _isLoading.value = false
        }
    }
}