package com.optic.pramosreservasappz.presentation.screens.business.mybusiness

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

// ─── Design Tokens ───────────────────────────────────────────────────────────
private val Cyan        = Color(0xFF22C1C3)
private val CyanDeep    = Color(0xFF0EA5A7)
private val CyanSoft    = Color(0xFFE6F9F9)
private val CyanGlow    = Color(0xFF22C1C3).copy(alpha = 0.18f)
private val Ink         = Color(0xFF0D1117)
private val InkLight    = Color(0xFF374151)
private val Muted       = Color(0xFF9CA3AF)
private val Surface     = Color(0xFFFFFFFF)
private val SurfaceAlt  = Color(0xFFF8FAFC)
private val Stroke      = Color(0xFFE5E9F0)
private val Gold        = Color(0xFFF59E0B)

@Composable
fun MyBusinessContent(
    paddingValues: PaddingValues,
    viewModel: BusinessViewModel,
    navController: NavHostController
) {
    val businessState by viewModel.businessState

    LaunchedEffect(Unit) {
        viewModel.getBusinessById(1, 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceAlt)
            .padding(paddingValues)
    ) {
        when (businessState) {

            is Resource.Loading -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(color = Cyan, strokeWidth = 2.5.dp)
                    Text("Cargando negocio...", color = Muted, fontSize = 13.sp)
                }
            }

            is Resource.Failure -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(36.dp))
                    Text("Error al cargar negocio", color = InkLight, fontSize = 14.sp)
                }
            }

            is Resource.Success -> {
                val business = (businessState as Resource.Success).data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    contentPadding = PaddingValues(bottom = 48.dp)
                ) {

                    // ── HERO HEADER ─────────────────────────────────────────
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Cyan, CyanDeep)
                                    )
                                )
                                .padding(horizontal = 24.dp, vertical = 32.dp)
                        ) {
                            // Decorative circle blobs
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 30.dp, y = (-20).dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.08f))
                            )
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.BottomStart)
                                    .offset(x = (-20).dp, y = 20.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.06f))
                            )

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Avatar with initials
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.22f))
                                            .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = business.name
                                                ?.take(2)
                                                ?.uppercase()
                                                ?: "MN",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }

                                    Spacer(Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "Mi negocio",
                                            color = Color.White.copy(alpha = 0.75f),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            business.name ?: "Sin nombre",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }

                                    // Edit button
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.15f))
                                            .clickable { },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(20.dp))

                                // Plan badge
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.18f))
                                        .border(
                                            1.dp,
                                            Color.White.copy(alpha = 0.35f),
                                            RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 14.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        business.plan.name,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // ── SECTION LABEL: INFORMACIÓN ──────────────────────────
                    item {
                        SectionLabel(
                            label = "Información del negocio",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        )
                    }

                    // ── INFO CARD (grouped) ──────────────────────────────────
                    item {
                        GroupedInfoCard(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            fields = listOf(
                                Triple(Icons.Outlined.Email,     "Email",     business.email),
                                Triple(Icons.Outlined.Phone,     "Teléfono",  business.phone),
                                Triple(Icons.Outlined.Phone,     "WhatsApp",  business.whatsappNumber),
                                Triple(Icons.Outlined.LocationOn,"Ciudad",    business.city),
                                Triple(Icons.Outlined.Public,    "País",      business.country)
                            )
                        )
                    }

                    // ── SECTION LABEL: PLAN ─────────────────────────────────
                    item {
                        SectionLabel(
                            label = "Tu plan activo",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        )
                    }

                    // ── PLAN CARD ────────────────────────────────────────────
                    item {
                        PlanCard(
                            planName = business.plan.name,
                            planPrice = business.plan.price.toString(),
                            planExplanation = business.plan.explanation,
                            maxCollab = business.plan.maxCollab,
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

// ─── Section Label ────────────────────────────────────────────────────────────
@Composable
private fun SectionLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label.uppercase(),
        modifier = modifier,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Muted,
        letterSpacing = 1.2.sp
    )
}

// ─── Grouped Info Card ───────────────────────────────────────────────────────
@Composable
private fun GroupedInfoCard(
    fields: List<Triple<ImageVector, String, String?>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(0.04f),
                spotColor = Color.Black.copy(0.06f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
    ) {
        fields.forEachIndexed { index, (icon, label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Icon container
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyanSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Cyan,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = Muted,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = value ?: "Sin completar",
                        fontSize = 14.sp,
                        color = if (value != null) Ink else Muted,
                        fontWeight = if (value != null) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }

            if (index < fields.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    thickness = 0.8.dp,
                    color = Stroke
                )
            }
        }
    }
}

// ─── Plan Card ───────────────────────────────────────────────────────────────
@Composable
private fun PlanCard(
    planName: String,
    planPrice: String,
    planExplanation: String,
    maxCollab: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(0.04f),
                spotColor = Color.Black.copy(0.06f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .padding(20.dp)
    ) {
        // Plan header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    planName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    planExplanation,
                    fontSize = 13.sp,
                    color = Muted,
                    lineHeight = 18.sp
                )
            }

            // Price pill
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(CyanSoft)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "\$$planPrice",
                        color = Cyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        HorizontalDivider(thickness = 0.8.dp, color = Stroke)
        Spacer(Modifier.height(14.dp))

        // Features row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Default.Group, contentDescription = null, tint = Cyan, modifier = Modifier.size(15.dp))
            Text(
                "Hasta $maxCollab colaborador${if (maxCollab != 1) "es" else ""}",
                fontSize = 13.sp,
                color = InkLight,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(18.dp))

        // Upgrade CTA
        UpgradeBadge(
            text = "Mejorar plan  →",
            navController = navController
        )
    }
}

// ─── Upgrade Badge ────────────────────────────────────────────────────────────
@Composable
fun UpgradeBadge(
    text: String,
    navController: NavHostController
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(colors = listOf(Cyan, CyanDeep))
            )
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
                navController.navigate(ClientScreen.Planes.route)
            }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
