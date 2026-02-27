package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.core.Config
import com.optic.pramosreservasappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramosreservasappz.data.dataSource.remote.interceptor.AppApiKeyInterceptor
import com.optic.pramosreservasappz.data.dataSource.remote.service.AuthService
import com.optic.pramosreservasappz.data.dataSource.remote.service.ExternalService
import com.optic.pramosreservasappz.data.dataSource.remote.service.ReservasService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    /** OkHttpClient principal con interceptor que refresca token automáticamente */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        datastore: AuthDatastore,
        appApiKeyInterceptor: AppApiKeyInterceptor
    ): OkHttpClient {

        // Retrofit temporal solo para refresh token (sin ciclo)
        val retrofitTemp = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authServiceTemp = retrofitTemp.create(AuthService::class.java)

        return OkHttpClient.Builder()
            .addInterceptor(appApiKeyInterceptor) // 👈 PRIMERO API KEY
            .addInterceptor { chain ->   // 👈 DESPUÉS JWT
            val originalRequest = chain.request()

            var authResponse = runBlocking { datastore.getData().first() }
            var token = authResponse.token

            // Request original con token
            var requestWithToken = originalRequest.newBuilder()
                .addHeader("Authorization", token ?: "")
                .build()

            var response = chain.proceed(requestWithToken)

            // Si expira, intentamos refresh
            if (response.code == 401) {
                response.close()
                val refreshToken = authResponse.refresh_token
                if (!refreshToken.isNullOrBlank()) {
                    try {
                        val newAuthResponse = runBlocking {
                            authServiceTemp.refresToken(
                                com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest(
                                    refreshToken
                                )
                            ).body()
                        }

                        if (newAuthResponse != null && !newAuthResponse.token.isNullOrBlank()) {
                            runBlocking { datastore.save(newAuthResponse) }
                            token = newAuthResponse.token

                            // Reintentamos request original con nuevo token
                            val retryRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", token)
                                .build()

                            response = chain.proceed(retryRequest)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            response
        }.build()
    }




    // --- Retrofit para auth ---
    @Provides
    @Singleton
    @Named("auth")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // POSTMAN - THUNDER CLIENT - RETROFIT
        return Retrofit
            .Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    // --- Retrofit para TEAMS ---
    @Provides
    @Singleton
    @Named("equipos")
    fun provideTeamsRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL_TEAMS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // --- Retrofit para EXTERNAL ---
    @Provides
    @Singleton
    @Named("external")
    fun provideExternalRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL_EXTERNAL_SERVICES)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()




    // -- Services para Auth
    @Provides
    @Singleton
    fun provideAuthService(@Named("auth") retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
    // -- Services para Teams
    @Provides
    @Singleton
    fun provideTeamService(@Named("equipos") retrofit: Retrofit): ReservasService =
        retrofit.create(ReservasService::class.java)


    // -- Services para External
    @Provides
    @Singleton
    fun provideExternalService(@Named("external") retrofit: Retrofit): ExternalService =
        retrofit.create(ExternalService::class.java)


}