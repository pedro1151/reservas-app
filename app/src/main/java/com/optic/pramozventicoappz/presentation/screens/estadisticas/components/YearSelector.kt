package com.optic.pramozventicoappz.presentation.screens.estadisticas.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Cyan
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Purple
import com.optic.pramozventicoappz.presentation.ui.theme.GradientBackground

@Composable
fun YearSelector(
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {

    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

    val years = remember {
        (0..9).map { currentYear - it }
    }

    var expanded by remember { mutableStateOf(false) }

    Box {

        Text(
            text = "📅 $selectedYear",
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
                .background(GradientBackground)
        ) {

            years.forEach { year ->

                DropdownMenuItem(
                    text = {
                        Text(year.toString(), color = Color.White)
                    },
                    onClick = {
                        expanded = false
                        onYearSelected(year) // 🔥 VM aquí
                    }
                )
            }
        }
    }
}