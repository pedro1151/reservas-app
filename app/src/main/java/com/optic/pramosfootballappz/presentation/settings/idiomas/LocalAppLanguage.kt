package com.optic.pramosfootballappz.presentation.settings.idiomas

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

val LocalAppLanguage = compositionLocalOf {
    mutableStateOf(AppLanguage.ES)
}
