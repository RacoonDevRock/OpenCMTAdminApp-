package com.cmt.openctmadminapp.research.ui.data

import com.cmt.openctmadminapp.research.ui.data.network.SearchClient
import com.cmt.openctmadminapp.research.ui.data.network.response.SolicitudDTOResponse
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchClient: SearchClient) {

    suspend fun searchSolicitudes(
        estadoStr: String?,
        periodoStr: String?,
        page: Int = 0,
        size: Int = 10,
    ): Response<List<SolicitudDTOResponse>> {
        val response = searchClient.searchIncidents(estadoStr, periodoStr, page, size)
        return if (response.isSuccessful) {
            val solicitudes = response.body()?.embedded?.solicitudDTOPreviewList ?: emptyList()
            Response.success(solicitudes)
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}