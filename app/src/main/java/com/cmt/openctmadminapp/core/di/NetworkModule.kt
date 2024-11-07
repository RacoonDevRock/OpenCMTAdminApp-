package com.cmt.openctmadminapp.core.di

import android.content.Context
import com.cmt.openctmadminapp.login.data.network.LoginClient
import com.cmt.openctmadminapp.report.detail.data.network.DetailClient
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.DetailIncidentClient
import com.cmt.openctmadminapp.research.data.network.SearchClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    @Named("AuthOkHttpClient")
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("NoAuthOkHttpClient")
    fun provideNoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @Named("RetrofitWithAuth")
    fun provideRetrofitWithAuth(@Named("AuthOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("http://<cambiar_red>:8081/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @Named("RetrofitNoAuth")
    fun provideRetrofitNoAuth(@Named("NoAuthOkHttpClient") noAuthOkHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://<cambiar_red>:8081/")
            .client(noAuthOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginClient(@Named("RetrofitNoAuth") retrofitNoAuth: Retrofit): LoginClient {
        return retrofitNoAuth.create(LoginClient::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchClient(@Named("RetrofitWithAuth") retrofitWithAuth: Retrofit): SearchClient {
        return retrofitWithAuth.create(SearchClient::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailClient(@Named("RetrofitWithAuth") retrofitWithAuth: Retrofit): DetailClient {
        return retrofitWithAuth.create(DetailClient::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailIncidentClient(@Named("RetrofitWithAuth") retrofitWithAuth: Retrofit): DetailIncidentClient {
        return retrofitWithAuth.create(DetailIncidentClient::class.java)
    }
}