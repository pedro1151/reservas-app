package com.optic.pramosreservasappz.presentation.util

import androidx.compose.ui.graphics.Color
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDateTime

fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF888888),
        Color(0xFF4A6CF7), Color(0xFF7C3AED), Color(0xFF059669),
        Color(0xFFDC2626), Color(0xFFD97706), Color(0xFF0891B2),
        Color(0xFFDB2777), Color(0xFF65A30D), Color(0xFF9333EA)
    )
    return colors[id % colors.size]
}

fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}



fun formatSaleDate(isoString: String): String {
    return try {
        // Parseamos la fecha ISO con zona UTC
        val zdt = ZonedDateTime.parse(isoString)

        // Creamos un formateador en español
        val formatter = DateTimeFormatter.ofPattern(
            "d 'de' MMMM 'de' yyyy, hh:mm a",
            Locale("es", "ES")
        )

        zdt.format(formatter)
    } catch (e: Exception) {
        isoString // fallback si algo falla
    }
}

fun getCurrentFormattedDate(): String {
    val now = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern(
        "d 'de' MMMM 'de' yyyy, hh:mm a",
        Locale("es", "ES")
    )

    return now.format(formatter)
}
