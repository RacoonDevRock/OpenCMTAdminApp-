package com.cmt.openctmadminapp.report.detail.data.network

import com.cmt.openctmadminapp.report.detail.data.network.response.SolicitudDTODetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DetailClient {

    @GET("api/solicitudes/{nroSolicitud}")
    suspend fun getSolicitudDetail(
        @Path("nroSolicitud") nroSolicitud: String,
        @Header("Authorization") authHeader: String = "Bearer",
    ): Response<SolicitudDTODetail>
}