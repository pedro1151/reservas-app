package com.optic.pramozventicoappz.presentation.screens.clients.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.presentation.components.dialogs.DefaultAlertDialog
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.getInitials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GRID_ACTION_WIDTH = 72.dp
private val Red500 = Color(0xFFEF4444)

@Composable
fun ClientGridCard(
    client: ClientResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val avatarColor = remember(client.id) { getAvatarColor(client.id) }
    val initials = remember(client.fullName) { getInitials(client.fullName) }

    val animatable = remember { Animatable(0f) }
    val offsetX = animatable.value
    val scope = rememberCoroutineScope()

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }

    val maxSwipe = -GRID_ACTION_WIDTH.value
    val snapThreshold = GRID_ACTION_WIDTH.value * 0.38f
    val isSwiped = offsetX < -2f
    val isCardOpen = offsetX <= -snapThreshold

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
                    dampingRatio = if (target == 0f) 0.88f else 0.92f,
                    stiffness = if (target == 0f) Spring.StiffnessLow else Spring.StiffnessMediumLow
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(24L * (client.id % 8))
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.96f),
        exit = fadeOut(tween(180)) + scaleOut(tween(180), targetScale = 0.96f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(primary)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(GRID_ACTION_WIDTH)
                        .fillMaxHeight()
                        .padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 6.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                            .background(primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (isCardOpen) {
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

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Editar",
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp))
                            .background(primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (isCardOpen) {
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

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "Borrar",
                            color = Color.White,
                            fontSize = 10.5.sp,
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
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
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
                                val shouldOpen = animatable.value <= -snapThreshold
                                snap(if (shouldOpen) maxSwipe else 0f)

                                scope.launch {
                                    delay(120)
                                    isDragging = false
                                }
                            },
                            onDragCancel = {
                                snap(0f)

                                scope.launch {
                                    delay(120)
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(avatarColor.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .size(28.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape,
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor = Color.Black.copy(alpha = 0.08f)
                                )
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.92f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (isSwiped) {
                                        snap(0f)
                                    } else {
                                        showMenu = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Más opciones",
                                tint = TextSecondary,
                                modifier = Modifier.size(15.dp)
                            )

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Editar",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            tint = primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        goToEdit()
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "Eliminar",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Red500
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteOutline,
                                            contentDescription = null,
                                            tint = Red500,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }

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
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = avatarColor.copy(alpha = 0.88f),
                                    letterSpacing = (-0.3).sp
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                top = 11.dp,
                                bottom = 11.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = client.fullName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 18.sp,
                            letterSpacing = (-0.1).sp,
                            textAlign = TextAlign.Center
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                        fontSize = 10.5.sp,
                                        color = TextSecondary.copy(alpha = 0.82f),
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(Modifier.height(6.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(999.dp))
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = null,
                                        tint = TextSecondary.copy(alpha = 0.72f),
                                        modifier = Modifier.size(11.dp)
                                    )

                                    Text(
                                        text = "Cliente",
                                        fontSize = 10.sp,
                                        color = TextSecondary.copy(alpha = 0.82f),
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.2.sp
                                    )
                                }
                            }
                        }
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