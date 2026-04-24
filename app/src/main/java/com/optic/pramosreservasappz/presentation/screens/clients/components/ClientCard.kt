package com.optic.pramosreservasappz.presentation.screens.clients.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue600  = Color(0xFF2563EB)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Red500   = Color(0xFFEF4444)

@Composable
fun ClientCard(
    client        : ClientResponse,
    navController : NavHostController,
    onDelete      : (ClientResponse) -> Unit,
    modifier      : Modifier = Modifier
) {
    var offsetX           by remember { mutableStateOf(0f) }
    var showDeleteDialog  by remember { mutableStateOf(false) }
    var visible           by remember { mutableStateOf(false) }

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
                .padding(vertical = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX != 0f
                ) { offsetX = 0f }
        ) {
            // ── Swipe action buttons ──
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Edit action pill
                AnimatedVisibility(
                    visible = offsetX <= editThreshold,
                    enter   = fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.80f),
                    exit    = fadeOut(tween(120)) + scaleOut(tween(120))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .shadow(
                                elevation    = 4.dp,
                                shape        = RoundedCornerShape(14.dp),
                                ambientColor = Blue600.copy(alpha = 0.20f),
                                spotColor    = Blue600.copy(alpha = 0.28f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF1D4ED8), Blue600)))
                            .clickable(remember { MutableInteractionSource() }, null) {
                                offsetX = 0f
                                navController.navigate(
                                    ClientScreen.ABMCliente.createRoute(
                                        clientId = client.id,
                                        editable = true
                                    )
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Edit, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                "Editar",
                                color         = Color.White,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }

                // Delete action pill
                AnimatedVisibility(
                    visible = offsetX <= deleteThreshold,
                    enter   = fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.80f),
                    exit    = fadeOut(tween(120)) + scaleOut(tween(120))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .shadow(
                                elevation    = 4.dp,
                                shape        = RoundedCornerShape(14.dp),
                                ambientColor = Red500.copy(alpha = 0.20f),
                                spotColor    = Red500.copy(alpha = 0.28f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Red500)
                            .clickable(remember { MutableInteractionSource() }, null) {
                                showDeleteDialog = true
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Delete, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                "Eliminar",
                                color         = Color.White,
                                fontSize      = 10.sp,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }
            }

            // ── Main card ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        scaleY = (1f - abs(offsetX) / 1000f).coerceIn(0.98f, 1f)
                    }
                    .shadow(
                        elevation    = 2.dp,
                        shape        = RoundedCornerShape(18.dp),
                        ambientColor = Blue600.copy(alpha = 0.05f)
                    )
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (offsetX == 0f) {
                            navController.navigate(
                                ClientScreen.ClientDetail.createRoute(clientId = client.id)
                            )
                        } else {
                            offsetX = 0f
                        }
                    },
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ── Avatar ──
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(avatarColor, avatarColor.copy(alpha = 0.70f))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = initials,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.width(13.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = client.fullName,
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Slate900,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(4.dp))

                        // Subtitle row
                        val subtitle = client.email ?: client.phone
                        if (subtitle != null) {
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    if (client.email != null) Icons.Outlined.Mail
                                    else Icons.Outlined.Phone,
                                    null,
                                    tint     = Slate400,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text     = subtitle,
                                    fontSize = 12.sp,
                                    color    = Slate400,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (offsetX == 0f) {
                        Icon(
                            Icons.Outlined.ChevronRight, null,
                            tint     = Slate200,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // ── Delete confirmation dialog ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; offsetX = 0f },
            title = {
                Text(
                    "¿Eliminar cliente?",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Slate900
                )
            },
            text = {
                Text(
                    "Se eliminará el perfil de ${client.fullName}. Esta acción no se puede deshacer.",
                    fontSize   = 14.sp,
                    color      = Slate400,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        visible = false
                        onDelete(client)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                    shape  = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; offsetX = 0f }) {
                    Text("Cancelar", color = Slate900)
                }
            },
            shape          = RoundedCornerShape(18.dp),
            containerColor = Color.White
        )
    }
}

// ─── Public helpers (also used by ClientContent grid card) ──────────────────────
fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1D4ED8), Color(0xFF2563EB), Color(0xFF7C3AED),
        Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706),
        Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF65A30D),
        Color(0xFF9333EA), Color(0xFF0F766E), Color(0xFFEA580C)
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
