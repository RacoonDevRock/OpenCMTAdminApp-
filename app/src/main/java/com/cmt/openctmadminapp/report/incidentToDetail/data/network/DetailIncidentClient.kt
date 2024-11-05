package com.cmt.openctmadminapp.report.incidentToDetail.data.network

import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.IncidenteDTODetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DetailIncidentClient {

    @GET("api/solicitudes/{nroSolicitud}/incidente")
    suspend fun getIncidenteDetail(
        @Path("nroSolicitud") nroSolicitud: String,
        @Header("Authorization") authHeader: String = "Bearer",
    ): Response<IncidenteDTODetail>
}