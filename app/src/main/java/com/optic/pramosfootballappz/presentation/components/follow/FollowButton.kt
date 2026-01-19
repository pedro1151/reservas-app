package com.optic.pramosfootballappz.presentation.components.follow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun FollowButton(
    onClick: () -> Unit = {},
    title: String = "Seguir",
    isFollowed: Boolean = true
) {

    // para idioma
    val localizedContext = LocalizedContext.current

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)) // 👈 Contorno blanco
    ) {
        Text(
            text = localizedContext.getString(R.string.ligas_screen_follow_button),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}