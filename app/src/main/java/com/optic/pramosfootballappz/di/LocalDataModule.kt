package com.optic.pramosfootballappz.di

import com.optic.pramosfootballappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramosfootballappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosfootballappz.data.dataSource.local.AuthLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    fun provideAuthLocalDataSource(authDatastore: AuthDatastore): AuthLocalDataSource = AuthLocalDataSourceImpl(authDatastore)

}