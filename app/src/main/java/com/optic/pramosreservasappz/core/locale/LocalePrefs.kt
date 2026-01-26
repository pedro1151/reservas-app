package com.optic.pramosreservasappz.core.locale

import android.content.Context

object LocalePrefs {

    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_LANG = "lang"

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANG, "es") ?: "es"
    }

    fun setLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, lang).apply()
    }
}
