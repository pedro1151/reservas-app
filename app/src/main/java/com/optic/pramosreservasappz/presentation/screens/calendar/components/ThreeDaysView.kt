package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun ThreeDaysView(
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    reservationsByDate: Map<LocalDate, List<Reservation>> = emptyMap()
) {
    val days = (0..2).map { selectedDate.plusDays(it.toLong()) }
    val hours = (0..23).map { LocalTime.of(it, 0) }
    val listState = rememberLazyListState()

    // Auto-scroll a hora actual
    LaunchedEffect(Unit) {
        val currentHour = LocalTime.now().hour
        val scrollTo = (currentHour - 1).coerceAtLeast(0)
        listState.scrollToItem(scrollTo)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Header con los 3 días
        ThreeDaysHeader(days = days, onDateSelect = onDateSelect)

        HorizontalDivider(color = Color(0xFFEEEEEE))

        // Grid con horas
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(
                count = hours.size,
                key = { it }
            ) { index ->
                val hour = hours[index]
                ThreeDaysTimeRow(
                    time = hour,
                    days = days,
                    reservationsByDate = reservationsByDate
                )
            }
        }
    }
}

@Composable
private fun ThreeDaysHeader(
    days: List<LocalDate>,
    onDateSelect: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 56.dp)  // Alinear con las horas
    ) {
        days.forEach { date ->
            val isSelected = date == today
            val dayLabel = date.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = if (isSelected) Color.Black else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF212121),
                        fontSize = 16.sp
                    )
                }
            }

            // Divisor vertical (excepto el último)
            if (date != days.last()) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(64.dp)
                        .background(Color(0xFFEEEEEE))
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun ThreeDaysTimeRow(
    time: LocalTime,
    days: List<LocalDate>,
    reservationsByDate: Map<LocalDate, List<Reservation>>
) {
    val formatter = DateTimeFormatter.ofPattern("H:mm")
    val isCurrentHour = LocalTime.now().hour == time.hour

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        // Columna de hora
        Box(
            modifier = Modifier
                .width(56.dp)
                .fillMaxHeight()
                .padding(end = 8.dp, top = 0.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            if (time.hour > 0) {
                Text(
                    text = time.format(formatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrentHour) MaterialTheme.colorScheme.primary
                    else Color(0xFF9E9E9E),
                    fontSize = 11.sp,
                    fontWeight = if (isCurrentHour) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        // Columnas de cada día
        days.forEachIndexed { index, date ->
            val dayReservations = reservationsByDate[date]
                ?.filter { it.startTime.hour == time.hour }
                ?: emptyList()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(
                        width = 0.5.dp,
                        color = Color(0xFFEEEEEE)
                    )
                    .background(
                        if (date == LocalDate.now())
                            Color(0xFFFAFAFA)
                        else Color.White
                    )
            ) {
                if (dayReservations.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        dayReservations.forEach { reservation ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                color = reservation.serviceColor.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    reservation.serviceColor.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = reservation.clientName,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
