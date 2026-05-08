package com.optic.pramozventicoappz.di

import com.optic.pramozventicoappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramozventicoappz.data.dataSource.remote.AuthRemoteDataSourceImpl
import com.optic.pramozventicoappz.data.dataSource.remote.ReservasRemoteDataSource
import com.optic.pramozventicoappz.data.dataSource.remote.ReservasRemoteDataSourceImpl
import com.optic.pramozventicoappz.data.dataSource.remote.service.AuthService
import com.optic.pramozventicoappz.data.dataSource.remote.service.ExternalService
import com.optic.pramozventicoappz.data.dataSource.remote.service.ReservasService
import com.optic.pramozventicoappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramozventicoappz.data.dataSource.remote.service.external.ExternalRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    fun provideAuthRemoteDataSource(authService: AuthService): AuthRemoteDataSource = AuthRemoteDataSourceImpl(authService)

    @Provides
    fun provideTeamRemoteDataSource(teamService: ReservasService): ReservasRemoteDataSource = ReservasRemoteDataSourceImpl(teamService)

    @Provides
    fun provideExternalRemoteDataSource(externalService: ExternalService): ExternalRemoteDataSource = ExternalRemoteDataSourceImpl(externalService)






}