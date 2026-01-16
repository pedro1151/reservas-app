@file:Suppress("DEPRECATION")

package com.optic.ecommerceappmvvm.core.locale

import android.content.Context
import android.content.res.Configuration
import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import kotlinx.coroutines.runBlocking
import java.util.Locale

object LocaleManager {

    fun applyLocale(context: Context, authDatastore: AuthDatastore) {
        val language = runBlocking {
            authDatastore.getSavedLanguage()
        }

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }
}