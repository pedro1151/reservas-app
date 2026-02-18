package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

enum class ReservationStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED
}

data class Reservation(
    val id: Int,
    val clientName: String,
    val serviceName: String,
    val serviceColor: Color,
    val startTime: LocalTime,
    val durationMinutes: Int,
    val status: ReservationStatus,
    val price: Double
)

@Composable
fun ReservationCard(
    reservation: Reservation,
    onEdit: (Reservation) -> Unit,
    onCancel: (Reservation) -> Unit,
    onStatusChange: (Reservation, ReservationStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val maxSwipeDistance = -200f
    val editThreshold = -60f
    val cancelThreshold = -140f

    val endTime = reservation.startTime.plusMinutes(reservation.durationMinutes.toLong())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 4.dp)
                .clickable(enabled = offsetX != 0f, onClick = { offsetX = 0f })
        ) {
            // Fondo con acciones swipe
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp)),
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .fillMaxHeight()
                            .background(Color(0xFF2196F3))
                            .clickable {
                                offsetX = 0f
                                onEdit(reservation)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Editar",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = offsetX <= cancelThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFEF5350))
                            .clickable { showCancelDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Cancelar",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Cancelar",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            // Card principal con swipe
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        val scale = 1f - (abs(offsetX) / 1000f)
                        scaleY = scale.coerceIn(0.98f, 1f)
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                offsetX = when {
                                    offsetX <= cancelThreshold -> maxSwipeDistance
                                    offsetX <= editThreshold -> -90f
                                    else -> 0f
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                offsetX = (offsetX + dragAmount).coerceIn(maxSwipeDistance, 0f)
                            }
                        )
                    }
                    .clickable {
                        if (offsetX == 0f) onEdit(reservation)
                        else offsetX = 0f
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 3.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Franja de color + horario
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(52.dp)
                                .background(
                                    reservation.serviceColor,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = reservation.startTime.format(timeFormatter),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = endTime.format(timeFormatter),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }

                    // Información principal
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = Color(0xFF9E9E9E)
                            )
                            Text(
                                text = reservation.clientName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Outlined.MiscellaneousServices,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = reservation.serviceColor
                            )
                            Text(
                                text = reservation.serviceName,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF757575),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = Color(0xFF9E9E9E)
                                )
                                Text(
                                    text = "${reservation.durationMinutes} min",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9E9E9E)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Bs",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9E9E9E),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "%.2f".format(reservation.price),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9E9E9E)
                                )
                            }
                        }
                    }

                    // Badge de estado
                    StatusBadge(status = reservation.status)
                }
            }
        }
    }

    if (showCancelDialog) {
        CancelDialog(
            clientName = reservation.clientName,
            serviceName = reservation.serviceName,
            onConfirm = {
                showCancelDialog = false
                visible = false
                onCancel(reservation)
            },
            onDismiss = {
                showCancelDialog = false
                offsetX = 0f
            }
        )
    }
}

@Composable
private fun StatusBadge(status: ReservationStatus) {
    val (icon, color, text) = when (status) {
        ReservationStatus.PENDING -> Triple(Icons.Outlined.Schedule, Color(0xFFFFA726), "Pendiente")
        ReservationStatus.CONFIRMED -> Triple(Icons.Outlined.CheckCircle, Color(0xFF4CAF50), "Confirmada")
        ReservationStatus.COMPLETED -> Triple(Icons.Filled.CheckCircle, Color(0xFF2196F3), "Completada")
        ReservationStatus.CANCELLED -> Triple(Icons.Outlined.Cancel, Color(0xFFEF5350), "Cancelada")
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(13.dp), tint = color)
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CancelDialog(
    clientName: String,
    serviceName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Outlined.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text("¿Cancelar reserva?", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Cliente: $clientName",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Servicio: $serviceName",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Esta acción notificará al cliente.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancelar reserva")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Volver") }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
