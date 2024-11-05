package com.cmt.openctmadminapp.research.ui.data.network.response

import com.google.gson.annotations.SerializedName

data class SolicitudDTOResponse(
    @SerializedName("nroSolicitud") val nroSolicitud: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("estado") val estado: String
)
