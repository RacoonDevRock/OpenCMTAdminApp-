package com.cmt.openctmadminapp.research.ui.data.network.response

import com.google.gson.annotations.SerializedName

data class SolicitudResponse(
    @SerializedName("_embedded") val embedded: EmbeddedIncidente? = null,
    @SerializedName("_links") val links: Links? = null,
    @SerializedName("page") val page: PageInfo? = null
)

data class EmbeddedIncidente(
    @SerializedName("solicitudDTOPreviewList") val solicitudDTOPreviewList: List<SolicitudDTOResponse> = emptyList()
)

data class Links(
    val first: Link? = null,
    val self: Link? = null,
    val next: Link? = null,
    val last: Link? = null
)

data class Link(val href: String)

data class PageInfo(
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int
)

