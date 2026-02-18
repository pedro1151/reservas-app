package com.optic.pramosreservasappz.presentation.screens.services.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlin.math.abs

@Composable
fun ServiceCard(
    service: ServiceResponse,
    navController: NavHostController,
    onDelete: (ServiceResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val serviceColor = remember(service.color) {
        if (!service.color.isNullOrBlank()) {
            try { Color(android.graphics.Color.parseColor(service.color)) }
            catch (e: Exception) { Color(0xFF2196F3) }
        } else Color(0xFF2196F3)
    }

    val initials = remember(service.name) { getServiceInitials(service.name) }

    val maxSwipeDistance = -200f
    val editThreshold = -60f
    val deleteThreshold = -140f

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp)
                .clickable(enabled = offsetX != 0f, onClick = { offsetX = 0f })
        ) {
            // Fondo swipe
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp)),
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(95.dp)
                            .fillMaxHeight()
                            .background(Color(0xFF2196F3))
                            .clickable {
                                offsetX = 0f
                                navController.navigate(
                                    ClientScreen.ABMServicio.createRoute(
                                        serviceId = service.id,
                                        editable = true
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "Editar",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = offsetX <= deleteThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(95.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFEF5350))
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "Eliminar",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Card principal
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
                                when {
                                    offsetX <= deleteThreshold -> offsetX = maxSwipeDistance
                                    offsetX <= editThreshold -> offsetX = -95f
                                    else -> offsetX = 0f
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                offsetX = (offsetX + dragAmount).coerceIn(maxSwipeDistance, 0f)
                            }
                        )
                    }
                    .clickable {
                        if (offsetX == 0f) {
                            navController.navigate(
                                ClientScreen.ABMServicio.createRoute(
                                    serviceId = service.id,
                                    editable = true
                                )
                            )
                        } else {
                            offsetX = 0f
                        }
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = RoundedCornerShape(11.dp),
                        color = serviceColor.copy(alpha = 0.15f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = initials,
                                style = MaterialTheme.typography.titleMedium,
                                color = serviceColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.width(13.dp))

                    // Información
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF000000),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(4.dp))

                        if (!service.description.isNullOrBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Description,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = Color(0xFF757575)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = service.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            service.price?.let {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF757575),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(Modifier.width(2.dp))
                                    Text(
                                        text = "%.2f".format(it),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF757575)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = Color(0xFF757575)
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    text = "${service.durationMinutes} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575)
                                )
                            }
                        }
                    }

                    if (offsetX == 0f) {
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFFE0E0E0),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteServiceDialog(
            serviceName = service.name,
            onConfirm = {
                showDeleteDialog = false
                visible = false
                onDelete(service)
            },
            onDismiss = {
                showDeleteDialog = false
                offsetX = 0f
            }
        )
    }
}

private fun getServiceInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> {
            val first = parts.first().firstOrNull()?.uppercase() ?: ""
            val last = parts.last().firstOrNull()?.uppercase() ?: ""
            "$first$last"
        }
    }
}

@Composable
private fun DeleteServiceDialog(
    serviceName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "¿Eliminar servicio?",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                Text(
                    text = "Estás a punto de eliminar:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = serviceName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}
