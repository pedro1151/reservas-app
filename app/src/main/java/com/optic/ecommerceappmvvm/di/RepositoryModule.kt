package com.optic.ecommerceappmvvm.di

import com.optic.ecommerceappmvvm.data.repository.AuthRepositoryImpl
import com.optic.ecommerceappmvvm.data.dataSource.local.AuthLocalDataSource
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixturePredictionDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.LeagueDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.PlayerDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.TeamDao
import com.optic.ecommerceappmvvm.data.dataSource.remote.AuthRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.TeamRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.ecommerceappmvvm.data.repository.ExternalRepositoryImpl
import com.optic.ecommerceappmvvm.data.repository.TeamRepositoryImpl
import com.optic.ecommerceappmvvm.data.repository.TriviasRepositoryImpl
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.ExternalRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository
import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        authRemoteDataSource: AuthRemoteDataSource,
        authLocalDataSource: AuthLocalDataSource
    ): AuthRepository = AuthRepositoryImpl(authRemoteDataSource, authLocalDataSource)



    @Provides
    fun provideTeamRepository(
        teamRemoteDataSource: TeamRemoteDataSource,
        daoFixtureDao: FixtureDao,
        leagueDao: LeagueDao,
        playerDao: PlayerDao,
        teamDao: TeamDao,
        fixturePredictionDao: FixturePredictionDao
    ): TeamRepository = TeamRepositoryImpl(
        teamRemoteDataSource,
        daoFixtureDao,
        leagueDao,
        playerDao,
        teamDao,
        fixturePredictionDao
    )


    @Provides
    fun provideExternalRepository(
        externalRemoteDataSource: ExternalRemoteDataSource,
    ): ExternalRepository = ExternalRepositoryImpl(externalRemoteDataSource)


    @Provides
    fun provideTriviasRepository(
        triviasRemoteDataSource: TriviasRemoteDataSource
    ): TriviasRepository = TriviasRepositoryImpl(triviasRemoteDataSource)



}