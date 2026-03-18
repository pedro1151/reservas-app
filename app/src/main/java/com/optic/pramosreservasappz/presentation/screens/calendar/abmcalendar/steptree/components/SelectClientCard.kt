package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptree.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import kotlin.math.abs

// ── Palette ────────────────────────────────────────────────────────────────────
private val SCCBlack   = Color(0xFF0D0D0D)
private val SCCGray100 = Color(0xFFF2F2F2)
private val SCCGray200 = Color(0xFFE8E8E8)
private val SCCGray400 = Color(0xFFAAAAAA)
private val SCCGray600 = Color(0xFF666666)

@Composable
fun SelectClientCard(
    client: ClientResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel
) {
    // ── lógica sin cambios ─────────────────────────────────────────────────────
    var offsetX          by remember { mutableStateOf(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getInitials(client.fullName) }

    val maxSwipeDistance = -200f
    val editThreshold    = -60f
    val deleteThreshold  = -140f

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX != 0f
                ) { offsetX = 0f }
        ) {
            // ── Fondos swipe ───────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth().matchParentSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter   = fadeIn() + expandHorizontally(),
                    exit    = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .background(SCCBlack)
                            .clickable {
                                offsetX = 0f
                                calendarViewModel.selectClientAndContinue(client.id)
                                navController.navigate(ClientScreen.CreateReservationStepFour.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(17.dp))
                            Text("Selec.", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.W500)
                        }
                    }
                }
                AnimatedVisibility(
                    visible = offsetX <= deleteThreshold,
                    enter   = fadeIn() + expandHorizontally(),
                    exit    = fadeOut() + shrinkHorizontally()
                ) {
                    Box(
                        modifier         = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .background(Color(0xFFEF5350))
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(17.dp))
                            Text("Borrar", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.W500)
                        }
                    }
                }
            }

            // ── Fila principal ─────────────────────────────────────────────────
            Row(
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
                                    offsetX <= editThreshold   -> -80f
                                    else                       -> 0f
                                }
                            },
                            onHorizontalDrag = { _, drag ->
                                offsetX = (offsetX + drag).coerceIn(maxSwipeDistance, 0f)
                            }
                        )
                    }
                    .background(Color.White)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (offsetX == 0f) {
                            calendarViewModel.selectClientAndContinue(client.id)
                            navController.navigate(ClientScreen.CreateReservationStepFour.route)
                        } else offsetX = 0f
                    }
                    .padding(horizontal = 16.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Avatar circular
                Box(
                    modifier         = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = initials,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.W700,
                        color      = Color.White,
                        letterSpacing = 0.3.sp
                    )
                }

                // Info
                Column(Modifier.weight(1f)) {
                    Text(
                        text          = client.fullName,
                        fontSize      = 14.sp,
                        fontWeight    = FontWeight.W500,
                        color         = SCCBlack,
                        maxLines      = 1,
                        overflow      = TextOverflow.Ellipsis,
                        letterSpacing = (-0.2).sp
                    )
                    val subtitle = client.email ?: client.phone
                    if (subtitle != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text     = subtitle,
                            fontSize = 12.sp,
                            color    = SCCGray400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    Icons.Outlined.ChevronRight, null,
                    tint     = SCCGray200,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Separador inferior alineado con el texto
            HorizontalDivider(
                color    = SCCGray100,
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 72.dp)
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; offsetX = 0f },
            shape            = RoundedCornerShape(18.dp),
            containerColor   = Color.White,
            title = {
                Text("¿Eliminar cliente?", fontSize = 16.sp, fontWeight = FontWeight.W600, color = SCCBlack)
            },
            text = {
                Text(
                    "Se eliminará el perfil de ${client.fullName}. Esta acción no se puede deshacer.",
                    fontSize = 14.sp,
                    color    = SCCGray600
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEF5350))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) { showDeleteDialog = false; visible = false; onDelete(client) }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text("Eliminar", fontSize = 13.sp, fontWeight = FontWeight.W600, color = Color.White)
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) { showDeleteDialog = false; offsetX = 0f }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text("Cancelar", fontSize = 13.sp, color = SCCGray600)
                }
            }
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
        else            -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
