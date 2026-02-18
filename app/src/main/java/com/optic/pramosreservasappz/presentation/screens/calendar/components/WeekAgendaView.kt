package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun WeekAgendaView(
    selectedDate: LocalDate,
    reservations: List<Reservation>,
    onDateSelect: (LocalDate) -> Unit
) {
    // Muestra 7 días desde el día seleccionado
    val days = (0..6).map { selectedDate.plusDays(it.toLong()) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(days) { date ->
                AgendaDaySection(
                    date = date,
                    reservations = if (date == selectedDate) reservations else emptyList(),
                    isToday = date == LocalDate.now()
                )
            }
        }

        // Barra de ingresos de la semana
        WeeklyEarningsBar(
            earnings = reservations.sumOf { it.price },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun AgendaDaySection(
    date: LocalDate,
    reservations: List<Reservation>,
    isToday: Boolean
) {
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    val dayNumber = date.dayOfMonth
    val month = date.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "${dayOfWeek.replaceFirstChar { it.uppercase() }}, $dayNumber $month",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isToday) Color(0xFF1565C0) else Color(0xFF212121)
        )

        Spacer(Modifier.height(6.dp))

        if (reservations.isEmpty()) {
            Text(
                text = "Nada planeado",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF9E9E9E)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                reservations.forEach { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onEdit = { },
                        onCancel = { },
                        onStatusChange = { _, _ -> }
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyEarningsBar(
    earnings: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Outlined.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF757575)
                )
                Text(
                    text = "Ingresos de esta semana",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242)
                )
            }
            Text(
                text = "Bs${"%.2f".format(earnings)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}
