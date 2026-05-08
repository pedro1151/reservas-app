package com.optic.pramozventicoappz.presentation.settings.idiomas

import com.optic.pramozventicoappz.data.dataSource.local.datastore.AuthDatastore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleEntryPoint {
    fun authDatastore(): AuthDatastore
}