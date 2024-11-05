package com.cmt.openctmadminapp.login.data.network.response

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("usuario") val usuario: String,
    @SerializedName("contrasenia") val contrasenia: String
)
