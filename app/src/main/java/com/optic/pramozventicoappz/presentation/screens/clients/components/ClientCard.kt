package com.optic.pramozventicoappz.presentation.screens.clients.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.presentation.components.dialogs.DefaultAlertDialog
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.ui.theme.Grafito
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.getInitials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ACTION_WIDTH = 64.dp

@Composable
fun ClientCard(
    client: ClientResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials = remember(client.fullName) { getInitials(client.fullName) }

    val animatable = remember { Animatable(0f) }
    val offsetX = animatable.value
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(20L * (client.id % 10))
        visible = true
    }

    val maxSwipe = -(ACTION_WIDTH.value * 2f)
    val editThreshold = -(ACTION_WIDTH.value * 0.70f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.40f)

    val isSwiped = offsetX < -2f

    val cardBgColor by animateColorAsState(
        targetValue = Color.White,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label = "clientCardBg"
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
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = if (target == 0f) 0.88f else Spring.DampingRatioNoBouncy,
                    stiffness = if (target == 0f) Spring.StiffnessLow else Spring.StiffnessMediumLow
                )
            )
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .height(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Grafito)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .background(primary.copy(alpha = 0.88f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (offsetX <= editThreshold) {
                                    snap(0f)
                                    goToEdit()
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Editar",
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxHeight()
                            .background(primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (offsetX <= deleteThreshold) {
                                    showDeleteDialog = true
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Borrar",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = offsetX.dp)
                    .shadow(
                        elevation = if (!isSwiped) 8.dp else 2.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor = Color.Black.copy(alpha = 0.08f),
                        clip = false
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBgColor)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isDragging = true },
                            onHorizontalDrag = { _, drag ->
                                scope.launch {
                                    val current = animatable.value
                                    val resistance = if (current < maxSwipe * 0.88f) 0.25f else 1f

                                    animatable.snapTo(
                                        (current + drag * resistance).coerceIn(maxSwipe, 0f)
                                    )
                                }
                            },
                            onDragEnd = {
                                val target = when {
                                    animatable.value <= deleteThreshold -> maxSwipe
                                    animatable.value <= editThreshold -> -ACTION_WIDTH.value
                                    else -> 0f
                                }

                                snap(target)

                                scope.launch {
                                    delay(100)
                                    isDragging = false
                                }
                            },
                            onDragCancel = {
                                snap(0f)

                                scope.launch {
                                    delay(100)
                                    isDragging = false
                                }
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (isDragging) return@clickable
                        if (isSwiped) snap(0f) else goToDetail()
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(86.dp)
                            .fillMaxHeight()
                            .background(avatarColor.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = RoundedCornerShape(21.dp),
                                        ambientColor = Color.Black.copy(alpha = 0.03f),
                                        spotColor = Color.Black.copy(alpha = 0.07f)
                                    )
                                    .clip(RoundedCornerShape(21.dp))
                                    .background(Color.White.copy(alpha = 0.88f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = avatarColor.copy(alpha = 0.88f),
                                    letterSpacing = 0.3.sp
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 14.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = client.fullName,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.1).sp
                        )

                        Spacer(Modifier.height(8.dp))

                        val subtitle = client.email ?: client.phone
                        if (subtitle != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (client.email != null) {
                                        Icons.Outlined.Mail
                                    } else {
                                        Icons.Outlined.Phone
                                    },
                                    contentDescription = null,
                                    tint = TextSecondary.copy(alpha = 0.72f),
                                    modifier = Modifier.size(11.dp)
                                )

                                Text(
                                    text = subtitle,
                                    fontSize = 12.sp,
                                    color = TextSecondary.copy(alpha = 0.82f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (!isSwiped) {
                        Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = null,
                            tint = TextSecondary.copy(alpha = 0.28f),
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DefaultAlertDialog(
            title = "¿Eliminar cliente?",
            message = "Se eliminará \"${client.fullName}\". Esta acción no se puede deshacer.",
            confirmText = "Eliminar cliente",
            dismissText = "Cancelar",
            icon = Icons.Default.Delete,
            onConfirm = {
                showDeleteDialog = false
                visible = false
                onDelete(client)
            },
            onDismiss = {
                showDeleteDialog = false
                snap(0f)
            }
        )
    }
}