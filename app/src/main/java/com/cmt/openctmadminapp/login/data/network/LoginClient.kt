package com.cmt.openctmadminapp.login.data.network

import com.cmt.openctmadminapp.login.data.network.response.AuthRequest
import com.cmt.openctmadminapp.login.data.network.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginClient {

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse
}