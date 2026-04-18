package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.core.Config
import com.optic.pramosreservasappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramosreservasappz.data.dataSource.remote.interceptor.AppApiKeyInterceptor
import com.optic.pramosreservasappz.data.dataSource.remote.service.AuthService
import com.optic.pramosreservasappz.data.dataSource.remote.service.ExternalService
import com.optic.pramosreservasappz.data.dataSource.remote.service.ReservasService
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
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

        // 🔹 Cliente SOLO para refresh (con API KEY, sin JWT)
        val okHttpRefresh = OkHttpClient.Builder()
            .addInterceptor(appApiKeyInterceptor)
            .build()

        val retrofitTemp = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpRefresh)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authServiceTemp = retrofitTemp.create(AuthService::class.java)

        return OkHttpClient.Builder()
            .addInterceptor(appApiKeyInterceptor) // ✅ API KEY SIEMPRE
            .addInterceptor { chain ->

                val originalRequest = chain.request()

                val authResponse = runBlocking { datastore.getData().first() }
                var token = authResponse.token

                val isRefreshCall =
                    originalRequest.url.encodedPath.contains("refresh")

                // 🔹 Construcción segura del request
                val requestBuilder = originalRequest.newBuilder()

                if (!isRefreshCall && !token.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                val requestWithToken = requestBuilder.build()

                var response = chain.proceed(requestWithToken)

                // 🔁 Intentar refresh si el token expiró
                if (response.code == 401 && !isRefreshCall) {
                    response.close()

                    val refreshToken = authResponse.refreshToken

                    if (!refreshToken.isNullOrBlank()) {
                        try {
                            val newAuthResponse = runBlocking {
                                authServiceTemp.refresToken(
                                    RefreshTokenRequest(refreshToken)
                                ).body()
                            }

                            if (newAuthResponse != null && !newAuthResponse.token.isNullOrBlank()) {

                                runBlocking { datastore.save(newAuthResponse) }

                                val retryRequest = originalRequest.newBuilder()
                                    .addHeader("Authorization", "Bearer ${newAuthResponse.token}")
                                    .build()

                                return@addInterceptor chain.proceed(retryRequest)
                            } else {
                                // ❌ REFRESH FALLÓ → LOGOUT
                                runBlocking { datastore.delete() }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()

                            // ❌ ERROR EN REFRESH → LOGOUT
                            runBlocking { datastore.delete() }
                        }
                    } else {
                        // ❌ NO HAY REFRESH TOKEN → LOGOUT
                        runBlocking { datastore.delete() }
                    }
                }

                response
            }
            .build()
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