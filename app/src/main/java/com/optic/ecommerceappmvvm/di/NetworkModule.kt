package com.optic.ecommerceappmvvm.di

import com.optic.ecommerceappmvvm.core.Config
import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.AuthService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.ExternalService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.TeamService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasService
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


    @Provides
    @Singleton
    fun provideOkHttpClient(datastore: AuthDatastore) = OkHttpClient.Builder().addInterceptor {
        val token = runBlocking {
            datastore.getData().first().token
        }
        val newRequest = it.request().newBuilder().addHeader("Authorization", token ?: "").build()
        it.proceed(newRequest)
    }.build()

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


    // --- Retrofit para Trivias
    @Provides
    @Singleton
    @Named("trivias")
    fun provideTriviasRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL_TRIVIAS)
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
    fun provideTeamService(@Named("equipos") retrofit: Retrofit): TeamService =
        retrofit.create(TeamService::class.java)


    // -- Services para External
    @Provides
    @Singleton
    fun provideExternalService(@Named("external") retrofit: Retrofit): ExternalService =
        retrofit.create(ExternalService::class.java)


    // -- Services para Trivias
    @Provides
    @Singleton
    fun provideTriviasService(@Named("trivias") retrofit: Retrofit): TriviasService =
        retrofit.create(TriviasService::class.java)
}