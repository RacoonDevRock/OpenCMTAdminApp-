package com.cmt.openctmadminapp.core.di

import android.content.Context
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.login.data.network.LoginClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenProvider(@ApplicationContext context: Context): TokenProvider {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return TokenProvider(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUnauthorized(): MutableSharedFlow<Unit> {
        return MutableSharedFlow()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider, unauthorized: MutableSharedFlow<Unit>): AuthInterceptor {
        return AuthInterceptor(tokenProvider, unauthorized)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("http://192.168.0.3:8081/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideLoginClient(retrofit: Retrofit): LoginClient {
        return retrofit.create(LoginClient::class.java)
    }
}