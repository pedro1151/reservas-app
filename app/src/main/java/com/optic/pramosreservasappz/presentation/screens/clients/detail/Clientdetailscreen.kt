package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink700  = Color(0xFFC2185B)
private val Pink600  = Color(0xFFE91E63)
private val Pink500  = Color(0xFFEC407A)
private val Pink400  = Color(0xFFF06292)
private val Pink50   = Color(0xFFFFF0F3)
private val Slate900 = Color(0xFF0F172A)
private val Slate700 = Color(0xFF334155)
private val Slate500 = Color(0xFF64748B)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val PageBg   = Color(0xFFF8F4F6)

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
            Box(
                Modifier
                    .fillMaxSize()
                    .background(PageBg),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CircularProgressIndicator(
                        color       = Pink600,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(30.dp)
                    )
                    Text(
                        "Cargando perfil...",
                        fontSize = 13.sp,
                        color    = Slate400
                    )
                }
            }
        }

        is Resource.Success -> {
            ClientDetailContent(client = state.data, navController = navController)
        }

        is Resource.Failure -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(PageBg),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier            = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Outlined.WifiOff, null,
                        tint     = Slate400,
                        modifier = Modifier.size(38.dp)
                    )
                    Text(
                        "No se pudo cargar el cliente",
                        color         = Slate500,
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.SemiBold
                    )
                }
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
    val initials    = getDetailInitials(client.fullName)
    val avatarColor = getDetailAvatarColor(client.id)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Slate100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.ArrowBack, null,
                                tint     = Slate700,
                                modifier = Modifier.size(18.dp)
                            )
                        }
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Pink50)
                                .border(1.dp, Pink400.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Edit, null,
                                tint     = Pink600,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    // Menú más opciones
                    IconButton(onClick = { }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Slate100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert, null,
                                tint     = Slate700,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(4.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = PageBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── White hero card ─────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Color.White)
                    .padding(bottom = 28.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Pink gradient strip at top
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Pink500.copy(alpha = 0f),
                                    Pink600, Pink700,
                                    Pink600,
                                    Pink500.copy(alpha = 0f)
                                )
                            )
                        )
                )

                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ── Avatar ──
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier         = Modifier.size(102.dp)
                    ) {
                        // Color halo
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(avatarColor.copy(alpha = 0.16f), Color.Transparent)
                                    )
                                )
                        )
                        // Avatar circle
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .shadow(
                                    elevation    = 10.dp,
                                    shape        = CircleShape,
                                    ambientColor = avatarColor.copy(alpha = 0.28f),
                                    spotColor    = avatarColor.copy(alpha = 0.38f)
                                )
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(avatarColor, avatarColor.copy(alpha = 0.72f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text          = initials,
                                fontSize      = 28.sp,
                                fontWeight    = FontWeight.Black,
                                color         = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // ── Nombre ──
                    Text(
                        text          = client.fullName,
                        fontSize      = 22.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = Slate900,
                        letterSpacing = (-0.5).sp
                    )

                    // Subtitle — email o ciudad/país
                    val subtitle = client.email
                        ?: listOfNotNull(client.city, client.country).joinToString(", ").ifBlank { null }
                    if (subtitle != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text     = subtitle,
                            fontSize = 13.sp,
                            color    = Slate400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Badge "Cliente"
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(Pink50, RoundedCornerShape(8.dp))
                            .border(1.dp, Pink400.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Person, null,
                                tint     = Pink600,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                "Cliente",
                                fontSize      = 11.sp,
                                color         = Pink600,
                                fontWeight    = FontWeight.SemiBold,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    // ── Iconos de acción rápida ──
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                    ) {
                        ActionIcon(
                            icon    = Icons.Outlined.CalendarMonth,
                            label   = "Cita",
                            tint    = Pink600,
                            bg      = Pink50,
                            ring    = Pink400.copy(alpha = 0.22f),
                            modifier = Modifier.weight(1f)
                        )
                        ActionIcon(
                            icon    = Icons.Outlined.ChatBubbleOutline,
                            label   = "Mensaje",
                            tint    = Color(0xFF7C3AED),
                            bg      = Color(0xFFF5F3FF),
                            ring    = Color(0xFF7C3AED).copy(alpha = 0.18f),
                            modifier = Modifier.weight(1f)
                        )
                        ActionIcon(
                            icon    = Icons.Outlined.Phone,
                            label   = "Llamar",
                            tint    = Color(0xFF059669),
                            bg      = Color(0xFFF0FDF4),
                            ring    = Color(0xFF059669).copy(alpha = 0.18f),
                            modifier = Modifier.weight(1f)
                        )
                        ActionIcon(
                            icon    = Icons.Outlined.Mail,
                            label   = "Email",
                            tint    = Color(0xFF0284C7),
                            bg      = Color(0xFFF0F9FF),
                            ring    = Color(0xFF0284C7).copy(alpha = 0.18f),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Campos de información ─────────────────────────────────────────
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Sección Contacto — solo si hay datos
                val hasPhone = !client.phone.isNullOrBlank()
                val hasEmail = !client.email.isNullOrBlank()
                if (hasPhone || hasEmail) {
                    InfoSection(title = "Contacto") {
                        client.phone?.let { phone ->
                            DetailField(
                                label  = "Teléfono",
                                value  = phone,
                                isLink = true,
                                icon   = Icons.Outlined.Phone,
                                tint   = Color(0xFF059669)
                            )
                        }
                        if (hasPhone && hasEmail) SectionDivider()
                        client.email?.let { email ->
                            DetailField(
                                label  = "Correo\nelectrónico",
                                value  = email,
                                isLink = true,
                                icon   = Icons.Outlined.Mail,
                                tint   = Color(0xFF0284C7)
                            )
                        }
                    }
                }

                // Sección con ciudad/país (mantiene la lógica original de "Empresa")
                if (!client.city.isNullOrBlank() || !client.country.isNullOrBlank()) {
                    val location = listOfNotNull(client.city, client.country).joinToString(", ")
                    InfoSection(title = "Ubicación") {
                        DetailField(
                            label  = "Empresa",
                            value  = location,
                            isLink = false,
                            icon   = Icons.Outlined.LocationOn,
                            tint   = Pink600
                        )
                    }
                }

                // Campo dirección
                InfoSection(title = "Notas") {
                    DetailField(
                        label       = "Dirección",
                        value       = "",
                        isLink      = true,
                        placeholder = "Agregar dirección",
                        icon        = Icons.Outlined.LocationOn,
                        tint        = Slate400
                    )
                    SectionDivider()
                    DetailField(
                        label       = "Notas",
                        value       = "",
                        isLink      = false,
                        placeholder = "Agregar tu nota",
                        icon        = Icons.Outlined.Notes,
                        tint        = Slate400
                    )
                }

                // ── Citas / Actualizaciones ──
                NavSection {
                    DetailNavRow(
                        title      = "Citas",
                        icon       = Icons.Outlined.CalendarMonth,
                        iconTint   = Pink600,
                        iconBg     = Pink50,
                        onClick    = { /* TODO: navegar a citas del cliente */ }
                    )
                    SectionDivider()
                    DetailNavRow(
                        title      = "Actualizaciones",
                        icon       = Icons.Outlined.Notifications,
                        iconTint   = Color(0xFF7C3AED),
                        iconBg     = Color(0xFFF5F3FF),
                        onClick    = { /* TODO */ }
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ─── Quick Action Button ─────────────────────────────────────────────────────────
@Composable
private fun ActionIcon(
    icon     : ImageVector,
    label    : String,
    tint     : Color,
    bg       : Color,
    ring     : Color,
    modifier : Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp), ambientColor = tint.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Slate200, RoundedCornerShape(16.dp))
            .clickable(remember { MutableInteractionSource() }, null) { }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(bg)
                .border(1.dp, ring, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Text(
            text          = label,
            fontSize      = 10.sp,
            color         = Slate500,
            fontWeight    = FontWeight.SemiBold,
            letterSpacing = 0.1.sp
        )
    }
}

// ─── Info Section Card ────────────────────────────────────────────────────────────
@Composable
private fun InfoSection(
    title   : String,
    content : @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier              = Modifier.padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Pink600)
            )
            Text(
                text          = title.uppercase(),
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Bold,
                color         = Pink600.copy(alpha = 0.80f),
                letterSpacing = 1.4.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(0.04f))
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.dp, Pink600.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content             = content
        )
    }
}

// ─── Navigation Section Card ─────────────────────────────────────────────────────
@Composable
private fun NavSection(
    content : @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier              = Modifier.padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Pink600)
            )
            Text(
                text          = "ACTIVIDAD",
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Bold,
                color         = Pink600.copy(alpha = 0.80f),
                letterSpacing = 1.4.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(18.dp), ambientColor = Color.Black.copy(0.04f))
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.dp, Pink600.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                .padding(vertical = 4.dp),
            content = content
        )
    }
}

// ─── Detail Field ────────────────────────────────────────────────────────────────
@Composable
private fun DetailField(
    label       : String,
    value       : String,
    isLink      : Boolean,
    icon        : ImageVector,
    tint        : Color,
    placeholder : String = ""
) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon chip
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(tint.copy(alpha = if (value.isBlank()) 0.06f else 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, null,
                tint     = if (value.isBlank()) tint.copy(alpha = 0.45f) else tint,
                modifier = Modifier.size(15.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text          = label,
                fontSize      = 11.sp,
                color         = Slate400,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 0.1.sp,
                lineHeight    = 14.sp
            )
            Spacer(Modifier.height(1.dp))
            val displayText  = value.ifBlank { placeholder }
            val displayColor = when {
                value.isBlank() -> Slate200
                isLink          -> Color(0xFF0D9488)   // mantiene el color teal original para links
                else            -> Slate900
            }
            Text(
                text          = displayText,
                fontSize      = 14.sp,
                color         = displayColor,
                fontWeight    = if (value.isBlank()) FontWeight.Normal else FontWeight.Medium,
                letterSpacing = (-0.1).sp,
                maxLines      = 2,
                overflow      = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Navigation Row ──────────────────────────────────────────────────────────────
@Composable
private fun DetailNavRow(
    title    : String,
    icon     : ImageVector,
    iconTint : Color,
    iconBg   : Color,
    onClick  : () -> Unit
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(remember { MutableInteractionSource() }, null, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(17.dp))
        }
        Text(
            text       = title,
            fontSize   = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Slate900,
            modifier   = Modifier.weight(1f)
        )
        Icon(
            Icons.Outlined.ChevronRight, null,
            tint     = Slate200,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ─── Hairline divider ────────────────────────────────────────────────────────────
@Composable
private fun SectionDivider() {
    HorizontalDivider(
        color     = Slate200,
        thickness = 0.5.dp,
        modifier  = Modifier.padding(horizontal = 4.dp)
    )
}

// ─── Helpers (sin cambios de lógica) ─────────────────────────────────────────────
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
        Color(0xFFE91E63), Color(0xFFC2185B), Color(0xFF9C27B0),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF009688),
        Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFFFF5722),
        Color(0xFF795548), Color(0xFF607D8B), Color(0xFFAD1457)
    )
    return colors[id % colors.size]
}
