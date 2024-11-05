package com.cmt.openctmadminapp.login.data

import com.cmt.openctmadminapp.login.data.network.LoginClient
import com.cmt.openctmadminapp.login.data.network.response.AuthRequest
import com.cmt.openctmadminapp.login.data.network.response.AuthResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginClient: LoginClient) {
    suspend fun login(usuario: String, contrasenia: String): AuthResponse {
        val request = AuthRequest(usuario, contrasenia)
        return loginClient.login(request)
    }
}