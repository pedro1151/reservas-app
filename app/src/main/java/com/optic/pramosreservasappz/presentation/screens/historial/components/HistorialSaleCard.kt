package com.optic.pramosreservasappz.presentation.screens.historial.components

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
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlinx.coroutines.launch
import kotlin.math.abs

// Ancho de cada acción — solo ícono + label, sin fondo de color
private val ACTION_WIDTH = 64.dp

@Composable
fun HistorialSaleCard(
    sale: SaleResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }
    val offsetX    = animatable.value
    val scope      = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }
    val initials    = "#" + sale.id.toString()

    // 64 + 64 = 128dp exacto → sin gap
    val maxSwipe        = -(ACTION_WIDTH.value * 2)
    val editThreshold   = -(ACTION_WIDTH.value * 0.8f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.5f)

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = if (target == 0f) Spring.DampingRatioNoBouncy
                    else              Spring.DampingRatioMediumBouncy,
                    stiffness    = Spring.StiffnessMedium
                )
            )
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX < -1f
                ) { snap(0f) }
        ) {

            // ── Acciones de fondo: fondo blanco, íconos de color ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // EDITAR — ícono azul
                if (offsetX <= editThreshold) {
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable {
                                snap(0f)
                                navController.navigate(
                                    ClientScreen.ABMCliente.createRoute(
                                        clientId = sale.id,
                                        editable = true
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint     = Color(0xFF2196F3),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Editar",
                                fontSize   = 11.sp,
                                color      = Color(0xFF2196F3),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // ELIMINAR — ícono rojo
                if (offsetX <= deleteThreshold) {
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint     = Color(0xFFE53935),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Eliminar",
                                fontSize   = 11.sp,
                                color      = Color(0xFFE53935),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // ── Card principal ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        scaleY = (1f - abs(offsetX) / 1200f).coerceIn(0.98f, 1f)
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val target = when {
                                    animatable.value <= deleteThreshold -> maxSwipe
                                    animatable.value <= editThreshold   -> -ACTION_WIDTH.value
                                    else                                -> 0f
                                }
                                snap(target)
                            },
                            onHorizontalDrag = { _, drag ->
                                scope.launch {
                                    animatable.snapTo(
                                        (animatable.value + drag).coerceIn(maxSwipe, 0f)
                                    )
                                }
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (offsetX > -1f) {
                            navController.navigate(
                                ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                            )
                        } else snap(0f)
                    },
                // Esquinas derechas planas al deslizar → acople perfecto con las acciones
                shape = if (offsetX < -2f)
                    RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                else
                    RoundedCornerShape(14.dp),
                color           = Color.White,
                shadowElevation = 0.dp,
                border          = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = initials,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = sale.description ?: "Venta",
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.Medium,
                            color         = Color.Black,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text     = sale.amount.toString(),
                            fontSize = 12.sp,
                            color    = Color(0xFFAAAAAA),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text          = "Vendedor",
                                fontSize      = 10.sp,
                                color         = Color(0xFF888888),
                                fontWeight    = FontWeight.Medium,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }

                    if (offsetX > -1f) {
                        Icon(
                            Icons.Outlined.ChevronRight, null,
                            tint     = Color(0xFFDDDDDD),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
            title = { Text("¿Eliminar venta?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
            text  = {
                Text(
                    "Se eliminará la venta \"${sale.description}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp, color = Color(0xFF555555)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; visible = false /* onDelete(sale) */ },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape   = RoundedCornerShape(10.dp)
                ) { Text("Eliminar", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; snap(0f) }) {
                    Text("Cancelar", color = Color.Black)
                }
            },
            shape          = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF888888),
        Color(0xFF4A6CF7), Color(0xFF7C3AED), Color(0xFF059669),
        Color(0xFFDC2626), Color(0xFFD97706), Color(0xFF0891B2),
        Color(0xFFDB2777), Color(0xFF65A30D), Color(0xFF9333EA)
    )
    return colors[id % colors.size]
}

fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
