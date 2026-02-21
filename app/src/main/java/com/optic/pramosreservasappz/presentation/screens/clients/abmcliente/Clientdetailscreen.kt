package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    navController: NavHostController,
    clientId: Int,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val oneClientState by viewModel.oneClientState.collectAsState()

    LaunchedEffect(clientId) {
        viewModel.getClientById(clientId)
    }

    when (val state = oneClientState) {
        is Resource.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        is Resource.Success -> {
            ClientDetailContent(client = state.data, navController = navController)
        }

        is Resource.Failure -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se pudo cargar el cliente", color = Color(0xFFAAAAAA), fontSize = 14.sp)
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClientDetailContent(
    client: ClientResponse,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val initials = getDetailInitials(client.fullName)
    val avatarColor = getDetailAvatarColor(client.id)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, null, tint = Color.Black)
                    }
                },
                title = {},
                actions = {
                    // Botón editar
                    IconButton(onClick = {
                        navController.navigate(
                            ClientScreen.ABMCliente.createRoute(
                                clientId = client.id,
                                editable = true
                            )
                        )
                    }) {
                        Icon(Icons.Outlined.Edit, null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                    // Menú más opciones
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.MoreVert, null, tint = Color.Black, modifier = Modifier.size(22.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

            // ── Avatar ──
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            // ── Nombre ──
            Text(
                text = client.fullName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                letterSpacing = (-0.3).sp
            )

            Spacer(Modifier.height(20.dp))

            // ── Iconos de acción rápida ──
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIcon(icon = Icons.Outlined.CalendarMonth, label = "Cita")
                ActionIcon(icon = Icons.Outlined.ChatBubbleOutline, label = "Mensaje")
                ActionIcon(icon = Icons.Outlined.Phone, label = "Llamar")
                ActionIcon(icon = Icons.Outlined.Mail, label = "Email")
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            // ── Campos de información ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                client.phone?.let { phone ->
                    DetailField(label = "Teléfono", value = phone, isLink = true)
                }
                client.email?.let { email ->
                    DetailField(label = "Correo\nelectrónico", value = email, isLink = true)
                }
                if (!client.city.isNullOrBlank() || !client.country.isNullOrBlank()) {
                    val location = listOfNotNull(client.city, client.country).joinToString(", ")
                    DetailField(label = "Empresa", value = location, isLink = false)
                }
                // Campo dirección (puedes conectar al modelo cuando lo tengas)
                DetailField(
                    label = "Dirección",
                    value = "",
                    isLink = true,
                    placeholder = "Agregar dirección"
                )
                DetailField(
                    label = "Notas",
                    value = "",
                    isLink = false,
                    placeholder = "Agregar tu nota"
                )
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            // ── Secciones Citas / Actualizaciones ──
            DetailNavRow(
                title = "Citas",
                onClick = { /* TODO: navegar a citas del cliente */ }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            DetailNavRow(
                title = "Actualizaciones",
                onClick = { /* TODO */ }
            )
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ActionIcon(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.Black, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun DetailField(
    label: String,
    value: String,
    isLink: Boolean,
    placeholder: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF888888),
            modifier = Modifier.width(90.dp),
            lineHeight = 18.sp
        )
        val displayText = value.ifBlank { placeholder }

        // ✅ NUEVO COLOR PARA LINKS - Teal moderno en lugar de azul
        val displayColor = when {
            value.isBlank() -> Color(0xFFBBBBBB)
            isLink -> Color(0xFF0D9488)  // ← Color Teal moderno
            else -> Color.Black
        }

        Text(
            text = displayText,
            fontSize = 14.sp,
            color = displayColor,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
}

@Composable
private fun DetailNavRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Icon(
            Icons.Outlined.ChevronRight,
            null,
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun getDetailInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun getDetailAvatarColor(id: Int): Color {
    val colors = listOf(
        Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFF4A6CF7),
        Color(0xFF7C3AED), Color(0xFF059669), Color(0xFFDC2626),
        Color(0xFFD97706), Color(0xFF0891B2), Color(0xFFDB2777),
        Color(0xFF65A30D), Color(0xFF9333EA), Color(0xFF888888)
    )
    return colors[id % colors.size]
}
