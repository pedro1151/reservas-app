package com.optic.pramosfootballappz.presentation.settings.idiomas


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.res.stringResource
import com.optic.pramosfootballappz.R


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
            // Orden alfabético por nombre mostrado
            LanguageOption("العربية", AppLanguage.AR, currentLanguage, onLanguageSelected) // Árabe
            LanguageOption("Deutsch", AppLanguage.DE, currentLanguage, onLanguageSelected) // Alemán
            LanguageOption("English", AppLanguage.EN, currentLanguage, onLanguageSelected) // Inglés
            LanguageOption("Español", AppLanguage.ES, currentLanguage, onLanguageSelected) // Español
            LanguageOption("Français", AppLanguage.FR, currentLanguage, onLanguageSelected) // Francés
            LanguageOption("हिन्दी", AppLanguage.HI, currentLanguage, onLanguageSelected) // Hindi
            LanguageOption("Bahasa Indonesia", AppLanguage.ID, currentLanguage, onLanguageSelected) // Indonesio
            LanguageOption("Italiano", AppLanguage.IT, currentLanguage, onLanguageSelected) // Italiano
            LanguageOption("日本語", AppLanguage.JA, currentLanguage, onLanguageSelected) // Japonés
            LanguageOption("한국어", AppLanguage.KO, currentLanguage, onLanguageSelected) // Coreano
            LanguageOption("Polski", AppLanguage.PL, currentLanguage, onLanguageSelected) // Polaco
            LanguageOption("Русский", AppLanguage.RU, currentLanguage, onLanguageSelected) // Ruso
            LanguageOption("Português", AppLanguage.PT, currentLanguage, onLanguageSelected) // Portugués
            LanguageOption("Türkçe", AppLanguage.TR, currentLanguage, onLanguageSelected) // Turco
            LanguageOption("Tiếng Việt", AppLanguage.VI, currentLanguage, onLanguageSelected) // Vietnamita
            LanguageOption("ไทย", AppLanguage.TH, currentLanguage, onLanguageSelected) // Tailandés
            LanguageOption("中文 (简体)", AppLanguage.ZH, currentLanguage, onLanguageSelected) // Chino simplificado
            LanguageOption("中文 (繁體)", AppLanguage.ZH_TW, currentLanguage, onLanguageSelected) // Chino tradicional
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
