package com.optic.pramosfootballappz.di

import com.optic.pramosfootballappz.data.repository.AuthRepositoryImpl
import com.optic.pramosfootballappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosfootballappz.data.dataSource.local.dao.FixtureDao
import com.optic.pramosfootballappz.data.dataSource.local.dao.FixturePredictionDao
import com.optic.pramosfootballappz.data.dataSource.local.dao.LeagueDao
import com.optic.pramosfootballappz.data.dataSource.local.dao.PlayerDao
import com.optic.pramosfootballappz.data.dataSource.local.dao.TeamDao
import com.optic.pramosfootballappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.TeamRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramosfootballappz.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.pramosfootballappz.data.repository.ExternalRepositoryImpl
import com.optic.pramosfootballappz.data.repository.TeamRepositoryImpl
import com.optic.pramosfootballappz.data.repository.TriviasRepositoryImpl
import com.optic.pramosfootballappz.domain.repository.AuthRepository
import com.optic.pramosfootballappz.domain.repository.ExternalRepository
import com.optic.pramosfootballappz.domain.repository.TeamRepository
import com.optic.pramosfootballappz.domain.repository.TriviasRepository
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