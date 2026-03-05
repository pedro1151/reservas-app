package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone.components

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
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectServiceCard(
    service: ServiceResponse,
    navController: NavHostController,
    onDelete: (ServiceResponse) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel
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
                            viewModel.selectServiceAndContinue(service.id)
                            navController.navigate(
                                ClientScreen.CreateReservationStepTwo.route
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
                                    Icons.Outlined.ChevronRight, null,
                                    tint = Color(0xFFBBBBBB),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }


                }
            }
        }
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


private fun getServiceInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
