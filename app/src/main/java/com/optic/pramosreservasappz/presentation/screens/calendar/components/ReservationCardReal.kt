package com.optic.pramosreservasappz.presentation.screens.calendar.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ReservationCardReal(
    reservation: ReservationResponseComplete
) {

    val formatter = DateTimeFormatter.ISO_DATE_TIME

    val start = try {
        LocalDateTime.parse(reservation.startTime, formatter)
    } catch (e: Exception) {
        null
    }

    val end = try {
        LocalDateTime.parse(reservation.endTime, formatter)
    } catch (e: Exception) {
        null
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {

            Text(
                text = reservation.client?.fullName ?: "Cliente",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = reservation.service?.name ?: "Servicio",
                color = Color(0xFF616161),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (start != null && end != null)
                    "${start.toLocalTime()} - ${end.toLocalTime()}"
                else
                    "Hora no disponible",
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )

            reservation.service?.price?.let {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Bs ${"%.2f".format(it)}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

