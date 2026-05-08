package com.optic.pramozventicoappz.di

import com.optic.pramozventicoappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramozventicoappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramozventicoappz.data.dataSource.local.AuthLocalDataSourceImpl
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