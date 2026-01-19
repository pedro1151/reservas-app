package com.optic.pramosfootballappz.di

import com.optic.pramosfootballappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.AuthRemoteDataSourceImpl
import com.optic.pramosfootballappz.data.dataSource.remote.TeamRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.TeamRemoteDataSourceImpl
import com.optic.pramosfootballappz.data.dataSource.remote.service.AuthService
import com.optic.pramosfootballappz.data.dataSource.remote.service.ExternalService
import com.optic.pramosfootballappz.data.dataSource.remote.service.TeamService
import com.optic.pramosfootballappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.service.external.ExternalRemoteDataSourceImpl
import com.optic.pramosfootballappz.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.service.trivias.TriviasRemoteDataSourceImpl
import com.optic.pramosfootballappz.data.dataSource.remote.service.trivias.TriviasService
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
    fun provideTeamRemoteDataSource(teamService: TeamService): TeamRemoteDataSource = TeamRemoteDataSourceImpl(teamService)

    @Provides
    fun provideExternalRemoteDataSource(externalService: ExternalService): ExternalRemoteDataSource = ExternalRemoteDataSourceImpl(externalService)

    @Provides
    fun provideTriviasRemoteDataSource(triviasService: TriviasService): TriviasRemoteDataSource = TriviasRemoteDataSourceImpl(triviasService)






}