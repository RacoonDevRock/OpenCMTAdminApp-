package com.cmt.openctmadminapp.core.di

import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider,
    private val onUnauthorized: MutableSharedFlow<Unit>,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider.getToken()

        if (token != null && originalRequest.header("Authorization") != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            val response = chain.proceed(newRequest)

            if (response.code() == 401) {
                tokenProvider.clearToken()
                onUnauthorized.tryEmit(Unit)
            }

            return response
        }
        return chain.proceed(originalRequest)
    }
}