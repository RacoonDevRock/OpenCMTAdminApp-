package com.cmt.openctmadminapp.research.ui.data.network

import com.cmt.openctmadminapp.research.ui.data.network.response.SolicitudResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchClient {

    @GET("api/solicitudes/buscar")
    suspend fun searchIncidents(
        @Query("estadoStr") estadoStr: String? = null,
        @Query("periodoStr") periodoStr: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Header("Authorization") authHeader: String = "Bearer"
    ): Response<SolicitudResponse>
}