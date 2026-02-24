package com.optic.pramosreservasappz.presentation.screens.clients.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChevronRight
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlin.math.abs

@Composable
fun ClientCard(
    client: ClientResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials = remember(client.fullName) { getInitials(client.fullName) }

    val maxSwipeDistance = -200f
    val editThreshold = -60f
    val deleteThreshold = -140f

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = offsetX != 0f
                ) { offsetX = 0f }
        ) {
            // Fondos de acciones swipe
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp)),
                horizontalArrangement = Arrangement.End
            ) {
                // Editar → va directo al form de edición con datos pre-cargados
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .background(Color(0xFF1A1A1A))
                            .clickable {
                                offsetX = 0f
                                navController.navigate(
                                    ClientScreen.ABMCliente.createRoute(
                                        clientId = client.id,
                                        editable = true
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Edit, null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text("Editar", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }

                // Eliminar
                AnimatedVisibility(
                    visible = offsetX <= deleteThreshold,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFEF5350))
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Delete, null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text("Eliminar", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Card principal — al tocar navega al DETALLE
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        scaleY = (1f - abs(offsetX) / 1000f).coerceIn(0.98f, 1f)
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                offsetX = when {
                                    offsetX <= deleteThreshold -> maxSwipeDistance
                                    offsetX <= editThreshold -> -80f
                                    else -> 0f
                                }
                            },
                            onHorizontalDrag = { _, drag ->
                                offsetX = (offsetX + drag).coerceIn(maxSwipeDistance, 0f)
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (offsetX == 0f) {
                            // Ir al DETALLE del cliente
                            navController.navigate(
                                ClientScreen.ClientDetail.createRoute(clientId = client.id)
                            )
                        } else {
                            offsetX = 0f
                        }
                    },
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = client.fullName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(3.dp))
                        val subtitle = client.email ?: client.phone
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                fontSize = 12.sp,
                                color = Color(0xFFAAAAAA),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (offsetX == 0f) {
                        Icon(
                            Icons.Outlined.ChevronRight, null,
                            tint = Color(0xFFDDDDDD),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; offsetX = 0f },
            title = {
                Text("¿Eliminar cliente?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text(
                    "Se eliminará el perfil de ${client.fullName}. Esta acción no se puede deshacer.",
                    fontSize = 14.sp,
                    color = Color(0xFF555555)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        visible = false
                        onDelete(client)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Eliminar", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; offsetX = 0f }) {
                    Text("Cancelar", color = Color.Black)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

private fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF888888),
        Color(0xFF4A6CF7), Color(0xFF7C3AED), Color(0xFF059669),
        Color(0xFFDC2626), Color(0xFFD97706), Color(0xFF0891B2),
        Color(0xFFDB2777), Color(0xFF65A30D), Color(0xFF9333EA)
    )
    return colors[id % colors.size]
}

private fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
