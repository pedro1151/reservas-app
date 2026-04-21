package com.optic.pramosreservasappz.presentation.screens.business.mybusiness.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BusinessInfoRow(
    label: String,
    value: String?
) {
    if (value == null) return

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary
        )
    }
}