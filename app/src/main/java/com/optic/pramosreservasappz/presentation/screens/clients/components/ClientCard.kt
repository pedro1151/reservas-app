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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlinx.coroutines.launch

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Slate900     = Color(0xFF0F172A)
private val Slate400     = Color(0xFF94A3B8)
private val Slate200     = Color(0xFFE2E8F0)
private val Red500       = Color(0xFFEF4444)
private val ACTION_WIDTH = 64.dp

@Composable
fun ClientCard(
    client        : ClientResponse,
    navController : NavHostController,
    onDelete      : (ClientResponse) -> Unit,
    modifier      : Modifier = Modifier
) {
    val primary     = MaterialTheme.colorScheme.primary
    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getInitials(client.fullName) }

    val animatable = remember { Animatable(0f) }
    val offsetX    = animatable.value
    val scope      = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }
    var isDragging       by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(20L * (client.id % 10))
        visible = true
    }

    val maxSwipe        = -(ACTION_WIDTH.value * 2f)
    val editThreshold   = -(ACTION_WIDTH.value * 0.7f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.4f)

    // Blanco en reposo → plomo suave al deslizar (igual que PrincipalProducCard)
    val cardBgColor by animateColorAsState(
        targetValue   = if (offsetX < -1f) Color(0xFFEEF0F2) else Color.White,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label         = "cardBg"
    )

    fun goToDetail() {
        navController.navigate(ClientScreen.ClientDetail.createRoute(clientId = client.id))
    }

    fun goToEdit() {
        navController.navigate(
            ClientScreen.ABMCliente.createRoute(clientId = client.id, editable = true)
        )
    }

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = when (target) {
                        0f       -> Spring.DampingRatioMediumBouncy
                        maxSwipe -> Spring.DampingRatioNoBouncy
                        else     -> Spring.DampingRatioLowBouncy
                    },
                    stiffness = when (target) {
                        0f   -> Spring.StiffnessMedium
                        else -> Spring.StiffnessMediumLow
                    }
                )
            )
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
        exit    = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {

            // ── Fondo blanco con botones estáticos detrás ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier              = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    // ─── Botón EDITAR
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) {
                                if (offsetX <= editThreshold) { snap(0f); goToEdit() }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .border(1.5.dp, primary, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint               = primary,
                                modifier           = Modifier.size(19.dp)
                            )
                        }
                    }

                    // ─── Botón ELIMINAR
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) {
                                if (offsetX <= deleteThreshold) showDeleteDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint               = Color.White,
                                modifier           = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }

            // ── Card principal — se desliza y adquiere tono plomo ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .shadow(
                        elevation    = if (offsetX > -1f) 4.dp else 2.dp,
                        shape        = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor    = Color.Black.copy(alpha = 0.08f),
                        clip         = false
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(cardBgColor)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isDragging = true },
                            onHorizontalDrag = { _, drag ->
                                scope.launch {
                                    val current    = animatable.value
                                    val resistance = if (current < maxSwipe * 0.88f) 0.25f else 1f
                                    animatable.snapTo(
                                        (current + drag * resistance).coerceIn(maxSwipe, 0f)
                                    )
                                }
                            },
                            onDragEnd = {
                                val target = when {
                                    animatable.value <= deleteThreshold -> maxSwipe
                                    animatable.value <= editThreshold   -> -ACTION_WIDTH.value
                                    else                                -> 0f
                                }
                                snap(target)
                                scope.launch {
                                    kotlinx.coroutines.delay(100)
                                    isDragging = false
                                }
                            },
                            onDragCancel = {
                                snap(0f)
                                scope.launch {
                                    kotlinx.coroutines.delay(100)
                                    isDragging = false
                                }
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (isDragging) return@clickable
                        if (offsetX < -1f) snap(0f) else goToDetail()
                    }
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ── Avatar sutil (fondo tenue + iniciales de color) ──
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(avatarColor.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = initials,
                            fontSize      = 16.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = avatarColor.copy(alpha = 0.88f),
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = client.fullName,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Slate900,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )

                        Spacer(Modifier.height(6.dp))

                        val subtitle = client.email ?: client.phone
                        if (subtitle != null) {
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (client.email != null) Icons.Outlined.Mail
                                    else Icons.Outlined.Phone,
                                    contentDescription = null,
                                    tint     = Slate400,
                                    modifier = Modifier.size(10.dp)
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

                    if (offsetX > -1f) {
                        Icon(
                            imageVector        = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint               = Slate200,
                            modifier           = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // ── Dialog de confirmación de borrado ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Delete,
                        contentDescription = null,
                        tint               = Red500,
                        modifier           = Modifier.size(22.dp)
                    )
                }
            },
            title = {
                Text(
                    text          = "¿Eliminar cliente?",
                    fontSize      = 17.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Slate900,
                    letterSpacing = (-0.3).sp
                )
            },
            text = {
                Text(
                    text       = "Se eliminará \"${client.fullName}\". Esta acción no se puede deshacer.",
                    fontSize   = 14.sp,
                    color      = Slate400,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick  = { showDeleteDialog = false; visible = false; onDelete(client) },
                    colors   = ButtonDefaults.buttonColors(containerColor = Red500),
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar cliente", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick  = { showDeleteDialog = false; snap(0f) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Slate400, fontWeight = FontWeight.Medium)
                }
            },
            shape          = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

// ─── Public helpers (también usados por el grid card en ClientContent) ──────────
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