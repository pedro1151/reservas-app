package com.optic.pramosreservasappz.presentation.screens.rapidsale.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun SmallCircleButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Brush
) {

    val BorderGray = Color(0xFFE5E7EB)

    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(backgroundColor)
           // .border(1.dp, BorderGray, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}