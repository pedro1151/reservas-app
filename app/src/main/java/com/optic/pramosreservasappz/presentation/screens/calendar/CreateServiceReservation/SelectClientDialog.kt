package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectClientDialog(
    onDismiss: () -> Unit,
    onClientSelected: (ClientResponse) -> Unit,
    clientViewModel: ClientViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val clients by clientViewModel.localClientsList.collectAsState()

    val filteredClients = remember(searchQuery, clients) {
        if (searchQuery.isBlank()) clients
        else clients.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.email?.contains(searchQuery, ignoreCase = true) == true ||
                    it.phone?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.99f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Seleccionar invitado(s)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 17.sp
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                placeholder = {
                    Text(
                        "Buscar",
                        fontSize = 15.sp,
                        color = Color(0xFFAAAAAA)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Color(0xFFAAAAAA),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = Color(0xFFAAAAAA),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Botón añadir nuevo cliente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* TODO: Navegar a ABMCliente */ }
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = Color(0xFF424242),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Añadir nuevo cliente",
                    fontSize = 15.sp,
                    color = Color(0xFF424242),
                    fontWeight = FontWeight.W400
                )
            }

            HorizontalDivider(
                color = Color(0xFFF0F0F0),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Lista de clientes
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp)
            ) {
                if (filteredClients.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Search,
                                    null,
                                    tint = Color(0xFFDDDDDD),
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    "Sin resultados",
                                    fontSize = 13.sp,
                                    color = Color(0xFFBBBBBB)
                                )
                            }
                        }
                    }
                } else {
                    items(filteredClients) { client ->
                        ClientItem(
                            client = client,
                            onClick = { onClientSelected(client) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientItem(
    client: ClientResponse,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(getAvatarColor(client.id), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(client.fullName),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = Color.White
            )
        }

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = client.fullName,
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black
            )
        }

        // Arrow
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFCCCCCC)
        )
    }
}

private fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun getAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF4A6CF7),
        Color(0xFF7C3AED), Color(0xFF059669), Color(0xFFDC2626),
        Color(0xFFD97706), Color(0xFF0891B2), Color(0xFFDB2777)
    )
    return colors[id % colors.size]
}
