package com.cmt.openctmadminapp.report.detail.data.network.response

import com.google.gson.annotations.SerializedName

data class SolicitudDTODetail(
    @SerializedName("nroSolicitud") val nroSolicitud: String,
    @SerializedName("fechaSolicitud") val fechaSolicitud: String,
    @SerializedName("horaSolicitud") val horaSolicitud: String,
    @SerializedName("solicitante") val solicitante: String,
    @SerializedName("identificacion") val identificacion: String,
    @SerializedName("domicilio") val domicilio: String,
    @SerializedName("correoElectronico") val correoElectronico: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("motivo") val motivo: String,
    @SerializedName("nroIncidente") val nroIncidente: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("_links") val links: Links
)

data class Links(
    @SerializedName("self") val self: Link,
    @SerializedName("todos") val todos: Link
)

data class Link(
    @SerializedName("href") val href: String
)
