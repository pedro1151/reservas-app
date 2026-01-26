package com.optic.pramosreservasappz.presentation.screens.services.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosreservasappz.R
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun ServiceSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    val localizedContext = LocalizedContext.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Buscar servicios",
                color = Color(0xFF9E9E9E) // gris moderno y minimalista
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFFB0B0B0) // gris suave para icono
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF7F7F7), // gris muy claro
            unfocusedContainerColor = Color(0xFFF7F7F7),
            disabledContainerColor = Color(0xFFF7F7F7),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = Color(0xFF333333),  // gris oscuro moderno
            unfocusedTextColor = Color(0xFF555555) // gris intermedio
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
