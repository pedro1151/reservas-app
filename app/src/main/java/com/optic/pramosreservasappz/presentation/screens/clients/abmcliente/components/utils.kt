package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF4CAF50), Color(0xFFFF5722), Color(0xFF9C27B0),
        Color(0xFF2196F3), Color(0xFF00BCD4), Color(0xFFFF9800),
        Color(0xFF673AB7), Color(0xFF009688), Color(0xFFE91E63),
        Color(0xFF3F51B5), Color(0xFFFFC107), Color(0xFFF44336)
    )
    return colors[id % colors.size]
}


fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> {
            val first = parts.first().firstOrNull()?.uppercase() ?: ""
            val last = parts.last().firstOrNull()?.uppercase() ?: ""
            "$first$last"
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    clientName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "¿Eliminar cliente?",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                Text(
                    text = "Estás a punto de eliminar a:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = clientName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
