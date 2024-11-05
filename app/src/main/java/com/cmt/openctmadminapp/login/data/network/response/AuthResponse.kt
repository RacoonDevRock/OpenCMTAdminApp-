package com.cmt.openctmadminapp.login.data.network.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token") val token: String
)
