package com.cmt.openctmadminapp.research.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cmt.openctmadminapp.research.data.network.response.SolicitudDTOResponse

class RequestPagingResource(
    private val repository: SearchRepository,
    private val estadoStr: String?,
    private val periodoStr: String?,
) : PagingSource<Int, SolicitudDTOResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SolicitudDTOResponse> {
        val page = params.key ?: 0
        return try {
            val response = repository.searchSolicitudes(
                estadoStr = estadoStr,
                periodoStr = periodoStr,
                page = page,
                size = params.loadSize
            )
            if (response.isSuccessful) {
                val incidents = response.body()?.embedded?.solicitudDTOPreviewList ?: emptyList()
                LoadResult.Page(
                    data = incidents,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (incidents.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SolicitudDTOResponse>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                anchor
            )?.nextKey?.minus(1)
        }
    }
}