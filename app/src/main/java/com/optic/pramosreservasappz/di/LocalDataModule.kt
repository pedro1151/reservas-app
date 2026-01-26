package com.optic.pramosreservasappz.di

import com.optic.pramosreservasappz.data.dataSource.local.datastore.AuthDatastore
import com.optic.pramosreservasappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosreservasappz.data.dataSource.local.AuthLocalDataSourceImpl
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