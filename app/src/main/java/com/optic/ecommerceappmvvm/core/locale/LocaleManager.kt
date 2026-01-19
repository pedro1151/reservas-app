
package com.optic.ecommerceappmvvm.core.locale

import android.content.Context
import android.content.res.Configuration
import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import kotlinx.coroutines.runBlocking
import java.util.Locale

object LocaleManager {

    fun attachBaseContext(context: Context): Context {
        val language = LocalePrefs.getLanguage(context)
        val locale = Locale(language)

        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
