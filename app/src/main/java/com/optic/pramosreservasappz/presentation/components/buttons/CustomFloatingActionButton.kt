package com.optic.pramosreservasappz.presentation.components.buttons


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,           // fondo negro
        contentColor = MaterialTheme.colorScheme.background,             // tinte del icono blanco
        shape = CircleShape,                    // forma redonda
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    ) {
        icon()
    }
}
