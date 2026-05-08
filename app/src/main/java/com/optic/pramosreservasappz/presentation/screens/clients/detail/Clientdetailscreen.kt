package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
private val Pink700    = Color(0xFFC2185B)
private val Pink600    = Color(0xFFE91E63)
private val Pink500    = Color(0xFFEC407A)
private val Pink50     = Color(0xFFFFF0F3)
private val TextPrimary   = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF94A3B8)
private val BorderGray    = Color(0xFFE2E8F0)

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
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color       = Pink600,
                    strokeWidth = 2.dp,
                    modifier    = Modifier.size(28.dp)
                )
            }
        }

        is Resource.Success -> {
            ClientDetailContent(client = state.data, navController = navController)
        }

        is Resource.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = "No se pudo cargar el cliente",
                    color    = TextSecondary,
                    fontSize = 14.sp
                )
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
    val avatarColor = remember(client.id) { getDetailAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getDetailInitials(client.fullName) }
    var expanded    by remember { mutableStateOf(false) }

    val rotate by animateFloatAsState(
        targetValue   = if (expanded) 180f else 0f,
        animationSpec = tween(180),
        label         = "expandRotation"
    )

    // Build location string
    val locationParts = listOfNotNull(
        client.address?.takeIf { it.isNotBlank() },
        client.city?.takeIf { it.isNotBlank() },
        client.state?.takeIf { it.isNotBlank() },
        client.country?.takeIf { it.isNotBlank() }
    )
    val locationText = locationParts.joinToString(", ").ifBlank { "-" }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                },
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                ClientScreen.ABMCliente.createRoute(
                                    clientId = client.id,
                                    editable = true
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint     = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "Más opciones",
                            tint     = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Avatar box ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .shadow(
                        elevation    = 10.dp,
                        shape        = RoundedCornerShape(28.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor    = Color.Black.copy(alpha = 0.08f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(listOf(avatarColor, avatarColor.copy(alpha = 0.72f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text          = initials,
                    fontSize      = 28.sp,
                    fontWeight    = FontWeight.Black,
                    color         = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            // ── Nombre ──
            Text(
                text      = client.fullName,
                fontSize  = 22.sp,
                fontWeight = FontWeight.Black,
                color     = TextPrimary,
                maxLines  = 2,
                overflow  = TextOverflow.Ellipsis
            )

            // ── Subtítulo (email o teléfono) ──
            val subtitle = client.email?.takeIf { it.isNotBlank() }
                ?: client.phone?.takeIf { it.isNotBlank() }
            if (subtitle != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text      = subtitle,
                    fontSize  = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color     = TextSecondary
                )
            }

            Spacer(Modifier.height(18.dp))

            // ── Chip "Cliente" ──
            ClientTypeChip()

            Spacer(Modifier.height(22.dp))

            // ── Card: Contacto ──────────────────────────────────────────────
            val hasPhone = !client.phone.isNullOrBlank()
            val hasEmail = !client.email.isNullOrBlank()
            if (hasPhone || hasEmail) {
                DetailCard {
                    DetailSectionTitle("Contacto")
                    Spacer(Modifier.height(8.dp))

                    if (hasPhone) {
                        DetailRow(label = "Teléfono", value = client.phone!!)
                    }
                    if (hasPhone && hasEmail) {
                        DetailDivider()
                    }
                    if (hasEmail) {
                        DetailRow(label = "Correo", value = client.email!!)
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            // ── Card: Información principal ─────────────────────────────────
            DetailCard {
                DetailRow(
                    label = "Empresa",
                    value = client.enterpriseName?.takeIf { it.isNotBlank() } ?: "-"
                )

                if (!client.address.isNullOrBlank() || locationParts.isNotEmpty()) {
                    DetailDivider()
                    DetailRow(label = "Ubicación", value = locationText)
                }

                DetailDivider()
                DetailRow(
                    label      = "Estado",
                    value      = "Activo",
                    valueColor = Color(0xFF059669)
                )
            }

            Spacer(Modifier.height(14.dp))

            // ── Secciones de actividad ──────────────────────────────────────
            DetailCard {
                DetailSectionTitle("Actividad")
                Spacer(Modifier.height(8.dp))

                DetailRow(
                    label = "Citas",
                    value = "Ver historial →"
                )
                DetailDivider()
                DetailRow(
                    label = "Actualizaciones",
                    value = "Ver historial →"
                )
            }

            Spacer(Modifier.height(14.dp))

            // ── Expandible: Información adicional ───────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation    = 8.dp,
                        shape        = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.03f),
                        spotColor    = Color.Black.copy(alpha = 0.07f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) { expanded = !expanded }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text      = "Información adicional",
                        fontSize  = 15.sp,
                        fontWeight = FontWeight.Black,
                        color     = TextPrimary,
                        modifier  = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint     = TextSecondary,
                        modifier = Modifier.size(22.dp).rotate(rotate)
                    )
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter   = expandVertically(tween(220)) + fadeIn(tween(180)),
                    exit    = shrinkVertically(tween(180)) + fadeOut(tween(120))
                ) {
                    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)) {
                        DetailDivider()
                        DetailRow(label = "Dirección",  value = client.address?.takeIf  { it.isNotBlank() } ?: "-")
                        DetailDivider()
                        DetailRow(label = "Ciudad",     value = client.city?.takeIf     { it.isNotBlank() } ?: "-")
                        DetailDivider()
                        DetailRow(label = "Provincia",  value = client.state?.takeIf    { it.isNotBlank() } ?: "-")
                        DetailDivider()
                        DetailRow(label = "País",       value = client.country?.takeIf  { it.isNotBlank() } ?: "-")
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ─── Chip ────────────────────────────────────────────────────────────────────────
@Composable
private fun ClientTypeChip() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Pink600.copy(alpha = 0.10f))
            .padding(horizontal = 13.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Pink600.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint     = Pink600,
                modifier = Modifier.size(11.dp)
            )
        }
        Spacer(Modifier.width(7.dp))
        Text(
            text       = "Cliente",
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            color      = Pink600
        )
    }
}

// ─── Card container ───────────────────────────────────────────────────────────────
@Composable
private fun DetailCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 8.dp,
                shape        = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor    = Color.Black.copy(alpha = 0.07f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun DetailSectionTitle(title: String) {
    Text(
        text       = title,
        fontSize   = 15.sp,
        fontWeight = FontWeight.Black,
        color      = TextPrimary
    )
}

@Composable
private fun DetailRow(
    label      : String,
    value      : String,
    valueColor : Color = TextPrimary
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment     = Alignment.Top
    ) {
        Text(
            text       = label,
            fontSize   = 13.sp,
            color      = TextSecondary,
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.width(105.dp)
        )
        Text(
            text       = value,
            fontSize   = 14.sp,
            color      = valueColor,
            fontWeight = FontWeight.SemiBold,
            modifier   = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DetailDivider() {
    HorizontalDivider(
        color     = BorderGray.copy(alpha = 0.65f),
        thickness = 0.8.dp
    )
}

// ─── Helpers ──────────────────────────────────────────────────────────────────────
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
