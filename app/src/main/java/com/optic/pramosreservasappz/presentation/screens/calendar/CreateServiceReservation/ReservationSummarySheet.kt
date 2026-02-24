package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationSummarySheet(
    service: ServiceResponse?,
    client: ClientResponse?,
    date: LocalDate?,
    time: LocalTime?,
    onDismiss: () -> Unit,
    onCreate: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.96f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Cita",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 17.sp
                )
                Button(
                    onClick = onCreate,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488) // Teal moderno
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Crear",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W600
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Servicio
                if (service != null) {
                    SummaryField(
                        icon = Icons.Outlined.MiscellaneousServices,
                        iconColor = Color(0xFF009688),
                        label = "Servicio",
                        value = service.name
                    )

                    // Costo y Duración
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(40.dp)
                    ) {
                        Column {
                            Text(
                                "Costo",
                                fontSize = 13.sp,
                                color = Color(0xFF757575),
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                "Bs ${service.price ?: 0}",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Column {
                            Text(
                                "Duración",
                                fontSize = 13.sp,
                                color = Color(0xFF757575),
                                fontWeight = FontWeight.W400
                            )
                            Text(
                                "${service.durationMinutes} minutos",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }

                // Fecha y hora
                if (date != null && time != null) {
                    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                    val endTime = time.plusMinutes(service?.durationMinutes?.toLong() ?: 0)
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                    SummaryField(
                        icon = Icons.Outlined.CalendarToday,
                        iconColor = Color(0xFF9E9E9E),
                        label = "Fecha",
                        value = "${dayOfWeek.replaceFirstChar { it.uppercase() }} ${date.dayOfMonth} ${
                            date.month.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                        } ${date.year} · ${time.format(timeFormatter)} - ${endTime.format(timeFormatter)}"
                    )
                }

                // Repetir
                SummaryField(
                    icon = Icons.Outlined.Repeat,
                    iconColor = Color(0xFF9E9E9E),
                    label = "Repetir",
                    value = "No se repite"
                )

                // Cliente
                if (client != null) {
                    Text(
                        text = "Seleccionar invitado(s)",
                        fontSize = 13.sp,
                        color = Color(0xFF757575),
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(start = 44.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 44.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE0E0E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getInitials(client.fullName),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                color = Color(0xFF424242)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = client.fullName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                            client.email?.let {
                                Text(
                                    text = it,
                                    fontSize = 13.sp,
                                    color = Color(0xFF757575)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                client.city?.let {
                                    Text(
                                        text = it,
                                        fontSize = 12.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                }
                                if (client.city != null && client.country != null) {
                                    Text(",", fontSize = 12.sp, color = Color(0xFF9E9E9E))
                                }
                                client.country?.let {
                                    Text(
                                        text = it,
                                        fontSize = 12.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                }
                            }
                        }

                        IconButton(onClick = { /* TODO: Quitar cliente */ }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Quitar",
                                tint = Color(0xFF9E9E9E),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Campos adicionales (placeholders)
                SummaryField(
                    icon = Icons.Outlined.Videocam,
                    iconColor = Color(0xFF9E9E9E),
                    label = "Añadir enlace de video",
                    value = ""
                )

                SummaryField(
                    icon = Icons.Outlined.Description,
                    iconColor = Color(0xFF9E9E9E),
                    label = "Notas para el invitado",
                    value = ""
                )

                SummaryField(
                    icon = Icons.Outlined.LocalOffer,
                    iconColor = Color(0xFF9E9E9E),
                    label = "Agregar etiqueta",
                    value = "Sin etiqueta"
                )

                SummaryField(
                    icon = null,
                    iconColor = Color.Transparent,
                    label = "Calendario",
                    value = "Jonathan Ticona Pérez",
                    showAvatar = true
                )
            }
        }
    }
}

@Composable
private fun SummaryField(
    icon: ImageVector?,
    iconColor: Color,
    label: String,
    value: String,
    showAvatar: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon or Avatar
        if (showAvatar) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF1976D2), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "JT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconColor
            )
        } else {
            Spacer(Modifier.width(20.dp))
        }

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color(0xFF757575),
                fontWeight = FontWeight.W400
            )
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W400
                )
            }
        }
    }
}

private fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
