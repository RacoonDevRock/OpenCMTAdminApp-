package com.cmt.openctmadminapp.report.incidentToDetail.data.network.response

import com.google.gson.annotations.SerializedName

data class IncidenteDTODetail(
    @SerializedName("nroIncidente") val nroIncidente: String,
    @SerializedName("fechaHora") val fechaHora: String,
    @SerializedName("tipoIncidente") val tipoIncidente: String,
    @SerializedName("zona") val zona: String,
    @SerializedName("sector") val sector: String,
    @SerializedName("tipoIntervencion") val tipoIntervencion: String,
    @SerializedName("resultadoIntervencion") val resultadoIntervencion: String,
    @SerializedName("detalle") val detalle: String,
    @SerializedName("personal") val personal: List<PersonalDTO>,
    @SerializedName("vehiculos") val vehiculos: List<VehiculoDTO>
)

data class PersonalDTO(
    @SerializedName("nombreCompleto") val nombreCompleto: String,
    @SerializedName("cargo") val cargo: String
)

data class VehiculoDTO(
    @SerializedName("numero") val numero: String,
    @SerializedName("placa") val placa: String
)