package com.optic.pramosreservasappz.presentation.screens.estadisticas.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.presentation.screens.estadisticas.SaleStatsViewModel
import com.optic.pramosreservasappz.presentation.screens.estadisticas.colors.Cyan
import com.optic.pramosreservasappz.presentation.screens.estadisticas.colors.Purple
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground

@Composable
fun ModeSelector(
    selectedMode: SaleStatsViewModel.StatsMode,
    onModeSelected: (SaleStatsViewModel.StatsMode) -> Unit
) {

    val modes = SaleStatsViewModel.StatsMode.values().toList()

    var expanded by remember { mutableStateOf(false) }

    Box {

        Text(
            text = "⚡ ${selectedMode.name}",
            color = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .background(Color.White.copy(alpha = 0.15f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                GradientBackground
            )
        ) {

            modes.forEach { mode ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = mode.name,
                            color = Color.White
                        )
                    },
                    onClick = {
                        expanded = false
                        onModeSelected(mode)
                    }
                )
            }
        }
    }
}