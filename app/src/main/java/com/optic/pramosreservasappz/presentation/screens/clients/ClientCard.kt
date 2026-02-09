package com.optic.pramosreservasappz.presentation.screens.clients.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

@Composable
fun ClientCard(
    client: ClientResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    // 🎨 Generar color único basado en el ID del cliente
    val avatarColor = remember(client.id) {
        getAvatarColor(client.id)
    }

    // 🎨 Obtener las iniciales del nombre
    val initials = remember(client.fullName) {
        getInitials(client.fullName)
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
                expandVertically(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200)) +
                shrinkVertically(animationSpec = tween(200))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate(
                        ClientScreen.ABMCliente.createRoute(
                            clientId = client.id,
                            editable = true
                        )
                    )
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp,
                pressedElevation = 3.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {

                // 🎨 Avatar con iniciales y color único
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = avatarColor
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                // 📋 Información del cliente
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 2.dp)
                ) {
                    // Nombre
                    Text(
                        text = client.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(6.dp))

                    // Email con ícono
                    client.email?.let {
                        InfoRow(
                            icon = Icons.Outlined.Email,
                            text = it,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    // Teléfono con ícono
                    client.phone?.let {
                        InfoRow(
                            icon = Icons.Outlined.Phone,
                            text = it,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    // Ubicación
                    if (!client.city.isNullOrBlank() || !client.country.isNullOrBlank()) {
                        Spacer(Modifier.height(2.dp))
                        InfoRow(
                            icon = Icons.Outlined.LocationOn,
                            text = listOfNotNull(client.city, client.country)
                                .joinToString(" · ")
                        )
                    }
                }

                // 📍 Menú de opciones
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Más opciones",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // 🔹 CORRECCIÓN: Usar MaterialTheme para el menú
                    MaterialTheme(
                        colorScheme = MaterialTheme.colorScheme.copy(
                            surface = Color.White,
                            onSurface = Color(0xFF212121)
                        )
                    ) {
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            offset = DpOffset((-8).dp, 0.dp),
                            modifier = Modifier
                                .width(180.dp)
                        ) {
                            // Editar
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            "Editar",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF212121)
                                        )
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(
                                        ClientScreen.ABMCliente.createRoute(
                                            clientId = client.id,
                                            editable = true
                                        )
                                    )
                                }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            // Eliminar
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            "Eliminar",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // 🗑️ Diálogo de confirmación de eliminación
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
            }
        )
    }
}

// 🎨 Función para generar color de avatar basado en ID
private fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF2196F3), // Azul
        Color(0xFF4CAF50), // Verde
        Color(0xFFFF9800), // Naranja
        Color(0xFF9C27B0), // Púrpura
        Color(0xFFF44336), // Rojo
        Color(0xFF00BCD4), // Cian
        Color(0xFFFF5722), // Naranja oscuro
        Color(0xFF673AB7), // Índigo
        Color(0xFF009688), // Teal
        Color(0xFFE91E63), // Rosa
        Color(0xFF3F51B5), // Azul índigo
        Color(0xFFFFC107)  // Ámbar
    )
    return colors[id % colors.size]
}

// 🎨 Función para obtener iniciales del nombre
private fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> {
            val first = parts.first().firstOrNull()?.uppercase() ?: ""
            val last = parts.last().firstOrNull()?.uppercase() ?: ""
            "$first$last"
        }
    }
}

// 📌 Componente para mostrar información con ícono
@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// 🗑️ Diálogo de confirmación de eliminación
@Composable
private fun DeleteConfirmationDialog(
    clientName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "¿Eliminar cliente?",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = "Estás a punto de eliminar a:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = clientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}
