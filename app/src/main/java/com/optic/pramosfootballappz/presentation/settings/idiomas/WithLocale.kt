package com.optic.pramosfootballappz.presentation.settings.idiomas

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.withLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(resources.configuration)
    config.setLocale(locale)

    return createConfigurationContext(config)
}
