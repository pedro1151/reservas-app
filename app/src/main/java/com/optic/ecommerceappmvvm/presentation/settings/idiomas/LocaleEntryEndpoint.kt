package com.optic.ecommerceappmvvm.presentation.settings.idiomas

import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleEntryPoint {
    fun authDatastore(): AuthDatastore
}