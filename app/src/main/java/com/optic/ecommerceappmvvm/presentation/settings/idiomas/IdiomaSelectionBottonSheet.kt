package com.optic.ecommerceappmvvm.presentation.settings.idiomas


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.res.stringResource
import com.optic.ecommerceappmvvm.presentation.ui.theme.AppThemeMode
import com.optic.ecommerceappmvvm.presentation.ui.theme.LocalAppTheme
import com.optic.ecommerceappmvvm.R


/// seleccionador de tema
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdiomaSelectionBottomSheet(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Text(
            text = stringResource(R.string.select_language),
            modifier = Modifier.padding(16.dp)
        )

        Column {
            LanguageOption("Español", AppLanguage.ES, currentLanguage, onLanguageSelected)
            LanguageOption("English", AppLanguage.EN, currentLanguage, onLanguageSelected)
            LanguageOption("中文", AppLanguage.ZH, currentLanguage, onLanguageSelected)
            LanguageOption("Português", AppLanguage.PT, currentLanguage, onLanguageSelected)
        }
    }
}

@Composable
private fun LanguageOption(
    label: String,
    language: AppLanguage,
    current: AppLanguage,
    onSelect: (AppLanguage) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = language == current,
            onClick = { onSelect(language) }
        )
        Text(label)
    }
}
