package com.cmt.openctmadminapp.report.incidentToDetail.data

import com.cmt.openctmadminapp.report.incidentToDetail.data.network.DetailIncidentClient
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.IncidenteDTODetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailIncidentRepository @Inject constructor(private val detailIncidentClient: DetailIncidentClient) {
    suspend fun getIncidenteDetail(nroSolicitud: String): Response<IncidenteDTODetail> {
        return withContext(Dispatchers.IO) {
            detailIncidentClient.getIncidenteDetail(nroSolicitud)
        }
    }
}