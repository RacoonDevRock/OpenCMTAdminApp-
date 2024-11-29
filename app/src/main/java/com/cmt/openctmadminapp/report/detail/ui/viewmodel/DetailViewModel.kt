package com.cmt.openctmadminapp.report.detail.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmt.openctmadminapp.report.detail.data.DetailRepository
import com.cmt.openctmadminapp.report.detail.data.network.response.MessageResponse
import com.cmt.openctmadminapp.report.detail.data.network.response.SolicitudDTODetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    private val _solicitudDetail = MutableStateFlow<SolicitudDTODetail?>(null)
    val solicitudDetail: StateFlow<SolicitudDTODetail?> = _solicitudDetail

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading

    private val _messageResponse = MutableStateFlow<MessageResponse?>(null)
    val messageResponse: StateFlow<MessageResponse?> = _messageResponse

    fun loadSolicitudDetail(nroSolicitud: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                detailRepository.getSolicitudDetail(nroSolicitud)
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

    fun approveSolicitud(nroSolicitud: String, onCompletion: () -> Unit) {
        handleSolicitudAction(nroSolicitud, "approve", onCompletion)
    }

    fun rejectSolicitud(nroSolicitud: String, onCompletion: () -> Unit) {
        handleSolicitudAction(nroSolicitud, "reject", onCompletion)
    }

    private fun handleSolicitudAction(
        nroSolicitud: String,
        accion: String,
        onCompletion: () -> Unit,
    ) {
        _isActionLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = detailRepository.atenderSolicitud(nroSolicitud, accion)
                if (response.isSuccessful) {
                    _messageResponse.value = response.body()
                } else {
                    Log.d("DetailViewModel", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: SocketTimeoutException) {
                _messageResponse.value =
                    MessageResponse("Error de tiempo de espera. Intenta nuevamente.")
            } finally {
                _isActionLoading.value = false
                withContext(Dispatchers.Main) {
                    onCompletion()
                }
            }
        }
    }
}