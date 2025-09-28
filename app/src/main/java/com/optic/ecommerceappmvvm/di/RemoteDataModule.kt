package com.optic.ecommerceappmvvm.di

import com.optic.ecommerceappmvvm.data.dataSource.remote.AuthRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.AuthRemoteDataSourceImpl
import com.optic.ecommerceappmvvm.data.dataSource.remote.TeamRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.TeamRemoteDataSourceImpl
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.AuthService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.ExternalService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.TeamService
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.external.ExternalRemoteDataSourceImpl
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasRemoteDataSourceImpl
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasService
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