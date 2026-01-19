package com.optic.ecommerceappmvvm.presentation.settings.idiomas

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

val LocalizedContext = compositionLocalOf<Context> {
    error("No LocalizedContext provided")
}