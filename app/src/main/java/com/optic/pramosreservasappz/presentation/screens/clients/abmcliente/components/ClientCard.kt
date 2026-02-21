package com.optic.pramosreservasappz.presentation.screens.clients.components

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
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.DeleteConfirmationDialog
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.getAvatarColor
import com.optic.pramosreservasappz.presentation.screens.clients.abmcliente.components.getInitials
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
                                    ClientScreen.ABMCliente.createRoute(
                                        clientId = client.id,
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
                                ClientScreen.ABMCliente.createRoute(
                                    clientId = client.id,
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
                        color = avatarColor
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = initials,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.width(13.dp))

                    // Información
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = client.fullName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF000000),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(4.dp))

                        client.email?.let {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = Color(0xFF757575)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            client.phone?.let {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = Color(0xFF757575)
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF757575),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            if (!client.city.isNullOrBlank() || !client.country.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.LocationOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = Color(0xFF757575)
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        text = listOfNotNull(client.city, client.country)
                                            .joinToString(" · "),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF757575),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
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
        DeleteConfirmationDialog(
            clientName = client.fullName,
            onConfirm = {
                showDeleteDialog = false
                visible = false
                onDelete(client)
            },
            onDismiss = {
                showDeleteDialog = false
                offsetX = 0f
            }
        )
    }
}

