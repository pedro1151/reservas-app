package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.data.repository.AuthRepositoryImpl
import com.optic.pramosreservasappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosreservasappz.data.dataSource.local.dao.FixtureDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.FixturePredictionDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.LeagueDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.PlayerDao
import com.optic.pramosreservasappz.data.dataSource.local.dao.TeamDao
import com.optic.pramosreservasappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramosreservasappz.data.dataSource.remote.ReservasRemoteDataSource
import com.optic.pramosreservasappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramosreservasappz.data.repository.ExternalRepositoryImpl
import com.optic.pramosreservasappz.data.repository.ReservasRepositoryImpl
import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ExternalRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
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
        teamRemoteDataSource: ReservasRemoteDataSource,
        daoFixtureDao: FixtureDao,
        leagueDao: LeagueDao,
        playerDao: PlayerDao,
        teamDao: TeamDao,
        fixturePredictionDao: FixturePredictionDao
    ): ReservasRepository = ReservasRepositoryImpl(
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





}