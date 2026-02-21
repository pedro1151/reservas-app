package com.optic.pramosreservasappz.presentation.screens.services.components

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(
    service: ServiceResponse,
    navController: NavHostController,
    onDelete: (ServiceResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showOptionsSheet by remember { mutableStateOf(false) }
    var isHidden by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    val serviceColor = remember(service.color) {
        if (!service.color.isNullOrBlank()) {
            try { Color(android.graphics.Color.parseColor(service.color)) }
            catch (e: Exception) { Color(0xFF4A6CF7) }
        } else Color(0xFF4A6CF7)
    }

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
            // ── Fondos de acciones swipe ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp)),
                horizontalArrangement = Arrangement.End
            ) {
                // Botón EDITAR (negro) ← NUEVO
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
                                    ClientScreen.ABMServicio.createRoute(
                                        serviceId = service.id,
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

                // Botón ELIMINAR (rojo)
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
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.height(3.dp))
                            Text("Eliminar", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }

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
                            navController.navigate(
                                ClientScreen.ServiceDetail.createRoute(serviceId = service.id)
                            )
                        } else offsetX = 0f
                    },
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEEEEEE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 8.dp)
                ) {
                    // Fila principal: avatar + info + tres puntos
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar con barra de color
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(serviceColor.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .fillMaxHeight()
                                    .background(serviceColor)
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = getServiceInitials(service.name),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = serviceColor
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                letterSpacing = (-0.2).sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = buildString {
                                    append("${service.durationMinutes} min")
                                    service.price?.let {
                                        if (it == 0.0) append(" · Gratis")
                                        else append(" · Bs ${it.toInt()}")
                                    }
                                },
                                fontSize = 12.sp,
                                color = Color(0xFFAAAAAA)
                            )
                        }

                        if (offsetX == 0f) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { showOptionsSheet = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.MoreVert, null,
                                    tint = Color(0xFFBBBBBB),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Botón copiar enlace — pequeño, esquina inferior derecha
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: copiar enlace */ },
                            modifier = Modifier.height(26.dp),
                            shape = RoundedCornerShape(7.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFDDDDDD)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF888888))
                        ) {
                            Icon(
                                Icons.Outlined.Link, null,
                                modifier = Modifier.size(11.dp),
                                tint = Color(0xFF999999)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Copiar enlace", fontSize = 10.sp, color = Color(0xFF999999))
                        }
                    }
                }
            }
        }
    }

    // ── Bottom sheet de opciones (tres puntos) ──
    if (showOptionsSheet) {
        ServiceOptionsSheet(
            serviceName = service.name,
            isHidden = isHidden,
            onDismiss = { showOptionsSheet = false },
            onEdit = {
                showOptionsSheet = false
                navController.navigate(
                    ClientScreen.ABMServicio.createRoute(serviceId = service.id, editable = true)
                )
            },
            onPreview = { showOptionsSheet = false },
            onShare = { showOptionsSheet = false },
            onToggleHidden = { isHidden = it },
            onDuplicate = { showOptionsSheet = false },
            onDelete = { showOptionsSheet = false; visible = false; onDelete(service) }
        )
    }

    // Diálogo eliminar desde swipe
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; offsetX = 0f },
            title = { Text("¿Borrar servicio?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
            text = { Text("Se eliminará \"${service.name}\". Esta acción no se puede deshacer.", fontSize = 14.sp, color = Color(0xFF555555)) },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; visible = false; onDelete(service) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Borrar", color = Color.White) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceOptionsSheet(
    serviceName: String,
    isHidden: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onPreview: () -> Unit,
    onShare: () -> Unit,
    onToggleHidden: (Boolean) -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = serviceName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Outlined.Close, null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
                }
            }

            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))

            OptionRow(icon = Icons.Outlined.Edit, label = "Editar", onClick = onEdit)
            OptionRow(icon = Icons.Outlined.Launch, label = "Vista previa", onClick = onPreview)

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))

            OptionRow(icon = Icons.Outlined.Share, label = "Compartir", onClick = onShare)

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))

            // Oculto con toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.VisibilityOff, null, tint = Color(0xFF555555), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(16.dp))
                Text("Establecer como oculto", fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
                Switch(
                    checked = isHidden,
                    onCheckedChange = onToggleHidden,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCCCCCC)
                    )
                )
            }

            OptionRow(icon = Icons.Outlined.ContentCopy, label = "Duplicado", onClick = onDuplicate)
            OptionRow(icon = Icons.Outlined.DeleteOutline, label = "Borrar", onClick = onDelete, tint = Color(0xFFEF5350))
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = Color(0xFF555555)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(label, fontSize = 15.sp, color = if (tint == Color(0xFF555555)) Color.Black else tint)
    }
}

private fun getServiceInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
