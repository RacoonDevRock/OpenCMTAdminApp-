package com.cmt.openctmadminapp.research.data

import com.cmt.openctmadminapp.research.data.network.SearchClient
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchClient: SearchClient) {

    suspend fun searchSolicitudes(
        estadoStr: String?,
        periodoStr: String?,
        page: Int = 0,
        size: Int = 10,
    ) = searchClient.searchIncidents(estadoStr, periodoStr, page, size)
}