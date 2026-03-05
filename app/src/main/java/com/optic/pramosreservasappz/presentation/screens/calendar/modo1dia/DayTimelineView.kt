package com.optic.pramosreservasappz.presentation.screens.calendar.modo1dia

import androidx.compose.foundation.background
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
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DayTimelineView(
    selectedDate: LocalDate,
    reservations: List<ReservationResponseComplete> = emptyList(),
    onTimeSlotClick: (LocalTime) -> Unit
) {

    val hours = (0..23).map { LocalTime.of(it, 0) }
    val listState = rememberLazyListState()

    val formatter = DateTimeFormatter.ISO_DATE_TIME

    // Agrupar reservas por fecha
    val reservationsByDate = remember(reservations) {
        reservations.groupBy {
            LocalDateTime.parse(it.startTime, formatter).toLocalDate()
        }
    }

    val dayReservations = reservationsByDate[selectedDate] ?: emptyList()

    // Auto scroll a hora actual
    LaunchedEffect(Unit) {
        val currentHour = LocalTime.now().hour
        val scrollTo = (currentHour - 1).coerceAtLeast(0)
        listState.scrollToItem(scrollTo)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {

        items(
            count = hours.size,
            key = { it }
        ) { index ->

            val hour = hours[index]

            val hourReservations = dayReservations.filter {

                val start = LocalDateTime.parse(
                    it.startTime,
                    DateTimeFormatter.ISO_DATE_TIME
                )

                start.hour == hour.hour
            }

            DayTimeSlot(
                time = hour,
                reservations = hourReservations,
                onSlotClick = { onTimeSlotClick(hour) }
            )
        }
    }
}

@Composable
private fun DayTimeSlot(
    time: LocalTime,
    reservations: List<ReservationResponseComplete>,
    onSlotClick: () -> Unit
) {

    val isCurrentHour = LocalTime.now().hour == time.hour
    val formatter = DateTimeFormatter.ofPattern("H:mm")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (reservations.isEmpty()) 64.dp else (64 + reservations.size * 72).dp)
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 56.dp),
            color = Color(0xFFEEEEEE),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            // Hora
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.TopEnd
            ) {

                Text(
                    text = if (time.hour == 0) "" else time.format(formatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrentHour)
                        MaterialTheme.colorScheme.primary
                    else Color(0xFF9E9E9E),
                    fontSize = 11.sp,
                    fontWeight = if (isCurrentHour) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Contenido
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {

                if (reservations.isNotEmpty()) {

                    Column(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        reservations.forEach { reservation ->

                            TimelineEventCard(reservation)

                        }
                    }
                }
            }
        }

        // Línea de hora actual
        if (isCurrentHour) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(
                        top = (LocalTime.now().minute * 64f / 60f).dp,
                        start = 48.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(5.dp)
                        )
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 1.5.dp
                )
            }
        }
    }
}

@Composable
private fun TimelineEventCard(
    reservation: ReservationResponseComplete
) {

    val start = LocalDateTime.parse(
        reservation.startTime,
        DateTimeFormatter.ISO_DATE_TIME
    )

    val end = LocalDateTime.parse(
        reservation.endTime,
        DateTimeFormatter.ISO_DATE_TIME
    )

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val serviceColor = parseColor(reservation.service?.color)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(6.dp),
        color = serviceColor.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            serviceColor.copy(alpha = 0.5f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Barra lateral color servicio
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        serviceColor,
                        RoundedCornerShape(
                            topStart = 6.dp,
                            bottomStart = 6.dp
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = reservation.client?.fullName ?: "Cliente",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 13.sp
                )

                Text(
                    text = reservation.service?.name ?: "Servicio",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575),
                    fontSize = 11.sp
                )
            }

            Text(
                text = "${start.format(timeFormatter)}-${end.format(timeFormatter)}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9E9E9E),
                fontSize = 10.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

fun parseColor(color: String?): Color {

    return try {

        if (color.isNullOrBlank())
            Color(0xFF90CAF9)
        else
            Color(android.graphics.Color.parseColor(color))

    } catch (e: Exception) {

        Color(0xFF90CAF9)
    }
}