package com.optic.pramozventicoappz.presentation.screens.clients

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

private val PageBg = Color(0xFFFAFAFA)

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
                    .background(PageBg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color       = MaterialTheme.colorScheme.primary,
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
                    .background(PageBg),
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
    val primary     = MaterialTheme.colorScheme.primary
    val avatarColor = remember(client.id) { getDetailAvatarColor(client.id) }
    val initials    = remember(client.fullName) { getDetailInitials(client.fullName) }
    var expanded    by remember { mutableStateOf(false) }

    val rotate by animateFloatAsState(
        targetValue   = if (expanded) 180f else 0f,
        animationSpec = tween(200),
        label         = "expandRotation"
    )

    val heroTint = primary.copy(alpha = 0.10f)

    val locationParts = listOfNotNull(
        client.address?.takeIf { it.isNotBlank() },
        client.city?.takeIf    { it.isNotBlank() },
        client.state?.takeIf   { it.isNotBlank() },
        client.country?.takeIf { it.isNotBlank() }
    )
    val locationText = locationParts.joinToString(", ").ifBlank { "—" }

    val hasPhone = !client.phone.isNullOrBlank()
    val hasEmail = !client.email.isNullOrBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    RoundIconButton(
                        icon = Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        onClick = { navController.popBackStack() }
                    )
                },
                title = {},
                actions = {
                    RoundIconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = "Más opciones",
                        onClick = { }
                    )
                    Spacer(Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = heroTint
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                primary = primary,
                onEdit = {
                    navController.navigate(
                        ClientScreen.ABMCliente.createRoute(
                            clientId = client.id,
                            editable = true
                        )
                    )
                }
            )
        },
        containerColor = PageBg
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {

            // ── HERO con avatar ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(heroTint, primary.copy(alpha = 0.04f), PageBg)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .offset(x = (-50).dp, y = (-30).dp)
                        .clip(CircleShape)
                        .background(primary.copy(alpha = 0.05f))
                        .align(Alignment.TopStart)
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 30.dp, y = 20.dp)
                        .clip(CircleShape)
                        .background(primary.copy(alpha = 0.04f))
                        .align(Alignment.BottomEnd)
                )

                // Avatar circular con iniciales
                Box(
                    modifier = Modifier
                        .size(124.dp)
                        .shadow(
                            elevation    = 24.dp,
                            shape        = CircleShape,
                            ambientColor = avatarColor.copy(alpha = 0.30f),
                            spotColor    = avatarColor.copy(alpha = 0.45f)
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
                        fontSize      = 38.sp,
                        fontWeight    = FontWeight.Black,
                        color         = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                }
            }

            // ── CONTENIDO ───────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                StatusBadge(isActive = true)

                Spacer(Modifier.height(12.dp))

                Text(
                    text          = client.fullName,
                    fontSize      = 26.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    color         = TextPrimary,
                    lineHeight    = 32.sp,
                    letterSpacing = (-0.6).sp,
                    maxLines      = 3,
                    overflow      = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text       = client.enterpriseName?.takeIf { it.isNotBlank() }
                        ?.let { "$it · Cliente" }
                        ?: "Cliente · Catálogo activo",
                    fontSize   = 13.sp,
                    color      = TextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(22.dp))

                // Quick info cards
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Outlined.Phone,
                        label    = "Teléfono",
                        value    = client.phone?.takeIf { it.isNotBlank() } ?: "—",
                        accent   = primary
                    )
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Outlined.Business,
                        label    = "Empresa",
                        value    = client.enterpriseName?.takeIf { it.isNotBlank() } ?: "—",
                        accent   = primary
                    )
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Outlined.LocationOn,
                        label    = "Ciudad",
                        value    = client.city?.takeIf { it.isNotBlank() } ?: "—",
                        accent   = primary
                    )
                }

                Spacer(Modifier.height(22.dp))

                // ── Card Contacto ─────────────────────────────────────────
                if (hasPhone || hasEmail) {
                    DetailCard {
                        SectionHeader(Icons.Outlined.Mail, "Contacto", primary)
                        Spacer(Modifier.height(8.dp))
                        if (hasPhone) {
                            DetailRow(label = "Teléfono", value = client.phone!!)
                        }
                        if (hasPhone && hasEmail) DetailDivider()
                        if (hasEmail) {
                            DetailRow(label = "Correo", value = client.email!!)
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }

                // ── Card Información principal ────────────────────────────
                DetailCard {
                    SectionHeader(Icons.Outlined.Person, "Información", primary)
                    Spacer(Modifier.height(8.dp))
                    DetailRow(
                        label = "Empresa",
                        value = client.enterpriseName?.takeIf { it.isNotBlank() } ?: "—"
                    )
                    if (locationParts.isNotEmpty()) {
                        DetailDivider()
                        DetailRow(label = "Ubicación", value = locationText)
                    }
                    DetailDivider()
                    DetailRow(
                        label      = "Estado",
                        value      = "Activo",
                        valueColor = ButtonSucessColor
                    )
                }

                Spacer(Modifier.height(14.dp))

                // ── Card Actividad ────────────────────────────────────────
                DetailCard {
                    SectionHeader(Icons.Outlined.History, "Actividad", primary)
                    Spacer(Modifier.height(8.dp))
                    DetailRow(label = "Citas",           value = "Ver historial →")
                    DetailDivider()
                    DetailRow(label = "Actualizaciones", value = "Ver historial →")
                }

                Spacer(Modifier.height(14.dp))

                // ── Expandible: Dirección completa ────────────────────────
                DetailCard(padding = 0.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) { expanded = !expanded }
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(primary.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn, null,
                                tint     = primary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text          = "Dirección completa",
                                fontSize      = 15.sp,
                                fontWeight    = FontWeight.ExtraBold,
                                color         = TextPrimary,
                                letterSpacing = (-0.2).sp
                            )
                            Text(
                                text     = if (expanded) "Toca para ocultar" else "Toca para ver más",
                                fontSize = 12.sp,
                                color    = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (expanded) primary.copy(alpha = 0.10f)
                                    else BorderGray.copy(alpha = 0.50f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ExpandMore,
                                contentDescription = null,
                                tint     = if (expanded) primary else TextSecondary,
                                modifier = Modifier.size(18.dp).rotate(rotate)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter   = expandVertically(tween(240)) + fadeIn(tween(200)),
                        exit    = shrinkVertically(tween(200)) + fadeOut(tween(140))
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start  = 18.dp,
                                end    = 18.dp,
                                bottom = 14.dp
                            )
                        ) {
                            HorizontalDivider(
                                color     = BorderGray.copy(alpha = 0.55f),
                                thickness = 0.8.dp,
                                modifier  = Modifier.padding(bottom = 4.dp)
                            )
                            AuditRow(
                                icon  = Icons.Outlined.LocationOn,
                                label = "Dirección",
                                value = client.address?.takeIf { it.isNotBlank() } ?: "—"
                            )
                            AuditRow(
                                icon  = Icons.Outlined.LocationCity,
                                label = "Ciudad",
                                value = client.city?.takeIf { it.isNotBlank() } ?: "—"
                            )
                            AuditRow(
                                icon  = Icons.Outlined.Map,
                                label = "Provincia",
                                value = client.state?.takeIf { it.isNotBlank() } ?: "—"
                            )
                            AuditRow(
                                icon  = Icons.Outlined.Language,
                                label = "País",
                                value = client.country?.takeIf { it.isNotBlank() } ?: "—"
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ─── Helpers UI ──────────────────────────────────────────────────────────────

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 12.dp)
            .size(40.dp)
            .shadow(4.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.06f))
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = TextPrimary,
            modifier           = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun StatusBadge(isActive: Boolean) {
    val color = if (isActive) ButtonSucessColor else TextSecondary
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.22f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text          = if (isActive) "ACTIVO" else "INACTIVO",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.ExtraBold,
            color         = color,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun QuickInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    accent: Color
) {
    Column(
        modifier = modifier
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.02f),
                spotColor    = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, BorderGray.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(accent.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
        }
        Text(
            text          = label,
            fontSize      = 11.sp,
            color         = TextSecondary,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
        Text(
            text          = value,
            fontSize      = 14.sp,
            fontWeight    = FontWeight.Bold,
            color         = TextPrimary,
            maxLines      = 1,
            overflow      = TextOverflow.Ellipsis,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun DetailCard(
    padding: Dp = 18.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 6.dp,
                shape        = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor    = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(padding),
        content = content
    )
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String, primary: Color) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = primary, modifier = Modifier.size(16.dp))
        }
        Text(
            text          = title,
            fontSize      = 15.sp,
            fontWeight    = FontWeight.ExtraBold,
            color         = TextPrimary,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary
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
private fun AuditRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(BorderGray.copy(alpha = 0.40f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(14.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = label,
                fontSize   = 11.sp,
                color      = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text       = value,
                fontSize   = 13.sp,
                color      = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailDivider() {
    HorizontalDivider(
        color     = BorderGray.copy(alpha = 0.65f),
        thickness = 0.8.dp
    )
}

@Composable
private fun BottomActionBar(primary: Color, onEdit: () -> Unit) {
    Surface(
        color           = Color.White,
        shadowElevation = 14.dp,
        modifier        = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation    = 12.dp,
                        shape        = RoundedCornerShape(16.dp),
                        ambientColor = primary.copy(alpha = 0.30f),
                        spotColor    = primary.copy(alpha = 0.40f)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(primary, primary.copy(alpha = 0.88f))
                        )
                    )
                    .clickable(onClick = onEdit),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                        tint     = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text          = "Editar cliente",
                        color         = Color.White,
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}

// ─── Helpers existentes (intactos) ───────────────────────────────────────────
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