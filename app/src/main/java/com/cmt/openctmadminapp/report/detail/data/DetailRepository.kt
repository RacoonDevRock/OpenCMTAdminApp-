package com.cmt.openctmadminapp.report.detail.data

import com.cmt.openctmadminapp.report.detail.data.network.DetailClient
import com.cmt.openctmadminapp.report.detail.data.network.response.SolicitudDTODetail
import retrofit2.Response
import javax.inject.Inject

class DetailRepository @Inject constructor(private val detailClient: DetailClient) {
    suspend fun getSolicitudDetail(id: String): Response<SolicitudDTODetail> {
        return detailClient.getSolicitudDetail(id)
    }
}