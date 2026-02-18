package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun MonthCalendar(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    reservationsByDate: Map<LocalDate, Int>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()

    // Calcular primer día de la semana (Lunes = 1, Domingo = 7)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Headers de días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Grid de días
        val weeks = (daysInMonth + firstDayOfWeek - 1) / 7 +
                if ((daysInMonth + firstDayOfWeek - 1) % 7 > 0) 1 else 0

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (week in 0 until weeks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayOfWeek in 1..7) {
                        val dayOfMonth = week * 7 + dayOfWeek - firstDayOfWeek + 1

                        if (dayOfMonth in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayOfMonth)
                            val isSelected = date == selectedDate
                            val isToday = date == today
                            val reservationCount = reservationsByDate[date] ?: 0

                            DayCell(
                                day = dayOfMonth,
                                isSelected = isSelected,
                                isToday = isToday,
                                reservationCount = reservationCount,
                                onClick = { onDateSelected(date) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            // Día vacío
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    reservationCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Número del día
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            isToday -> MaterialTheme.colorScheme.primaryContainer
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isSelected -> Color.White
                        isToday -> MaterialTheme.colorScheme.primary
                        else -> Color(0xFF000000)
                    },
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Indicador de reservas
            if (reservationCount > 0 && !isSelected) {
                Spacer(Modifier.height(2.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(minOf(reservationCount, 3)) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}
