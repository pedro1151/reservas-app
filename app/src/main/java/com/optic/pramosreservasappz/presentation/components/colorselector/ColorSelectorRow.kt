package com.optic.pramosreservasappz.presentation.components.colorselector

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val ModernColors = listOf(
    "#3F51B5", // Indigo
    "#2196F3", // Azul
    "#03A9F4", // Celeste
    "#009688", // Teal
    "#4CAF50", // Verde
    "#F44336", // Rojo
    "#E91E63", // Rosa
    "#9C27B0", // Morado
    "#673AB7", // Deep Purple
    "#FF9800"  // Naranja
)

@Composable
fun ColorSelectorRow(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    label: String,
    colors: List<String> = ModernColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        // ✅ Label arriba a la izquierda (igual a tus inputs)
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall, // un poco más grande
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ✅ Fila scrolleable horizontal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEach { hex ->
                val isSelected = hex.equals(selectedColor, ignoreCase = true)

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor(hex)),
                            shape = CircleShape
                        )
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable {
                            onColorSelected(hex)
                        }
                )
            }
        }
    }
}
