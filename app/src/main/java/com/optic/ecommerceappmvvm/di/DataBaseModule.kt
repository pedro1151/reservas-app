package com.optic.ecommerceappmvvm.di

import android.content.Context
import androidx.room.Room
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        )
            .fallbackToDestructiveMigration()  // 🔥 evita crashes por schema changes
            .build()

    @Provides
    fun provideFixtureDao(db: AppDatabase): FixtureDao = db.fixtureDao()
}
