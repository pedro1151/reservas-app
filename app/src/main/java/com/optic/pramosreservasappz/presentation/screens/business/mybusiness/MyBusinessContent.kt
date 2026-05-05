package com.optic.pramosreservasappz.presentation.screens.business.mybusiness

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Storefront
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

private val Bg = Color(0xFFF9FAFB)
private val CardBg = Color.White
private val Success = Color(0xFF10B981)
private val WarningSoft = Color(0xFFFFF7ED)
private val WarningColor = Color(0xFFF97316)

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
            .background(Bg)
            .padding(paddingValues)
    ) {
        when (businessState) {
            is Resource.Loading -> {
                LoadingBusinessState()
            }

            is Resource.Failure -> {
                ErrorBusinessState()
            }

            is Resource.Success -> {
                val business = (businessState as Resource.Success).data

                val missingFields = listOf(
                    business.email,
                    business.phone,
                    business.whatsappNumber,
                    business.city,
                    business.country
                ).count { it.isNullOrBlank() }

                val completionPercent = (((5 - missingFields) / 5f) * 100).toInt()
                val isComplete = missingFields == 0

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 42.dp)
                ) {
                    item {
                        BusinessHeroCard(
                            businessName = business.name ?: "Sin nombre",
                            planName = business.plan.name,
                            completionPercent = completionPercent,
                            isComplete = isComplete,
                            onEditClick = {
                                // TODO: navegar a editar negocio cuando tengas la ruta
                            }
                        )
                    }

                    item {
                        QuickBusinessStatus(
                            isComplete = isComplete,
                            missingFields = missingFields,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
                        )
                    }

                    item {
                        SectionLabel(
                            label = "Información del negocio",
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        GroupedInfoCard(
                            modifier = Modifier.padding(horizontal = 18.dp),
                            fields = listOf(
                                BusinessField(Icons.Outlined.Email, "Email", business.email),
                                BusinessField(Icons.Outlined.Phone, "Teléfono", business.phone),
                                BusinessField(Icons.Outlined.Phone, "WhatsApp", business.whatsappNumber),
                                BusinessField(Icons.Outlined.LocationOn, "Ciudad", business.city),
                                BusinessField(Icons.Outlined.Public, "País", business.country)
                            )
                        )
                    }

                    item {
                        MiniBusinessUpgradeBanner(
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp)
                        )
                    }

                    item {
                        SectionLabel(
                            label = "Tu plan activo",
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        PlanCard(
                            planName = business.plan.name,
                            planPrice = business.plan.price.toString(),
                            planExplanation = business.plan.explanation,
                            maxCollab = business.plan.maxCollab,
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun LoadingBusinessState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.8.dp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Cargando negocio...",
            color = TextSecondary,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun ErrorBusinessState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "No pudimos cargar tu negocio",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Revisa tu conexión e intenta nuevamente.",
            color = TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BusinessHeroCard(
    businessName: String,
    planName: String,
    completionPercent: Int,
    isComplete: Boolean,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        Color(0xFFD81B60)
                    )
                )
            )
            .padding(horizontal = 22.dp, vertical = 26.dp)
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.TopEnd)
                .offset(x = 34.dp, y = (-28).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .border(
                            width = 1.4.dp,
                            color = Color.White.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = businessName.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mi negocio",
                        color = Color.White.copy(alpha = 0.72f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = businessName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.5).sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar negocio",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HeroPill(
                    icon = Icons.Default.WorkspacePremium,
                    text = planName,
                    tint = AmarrilloSuave
                )

                HeroPill(
                    icon = if (isComplete) Icons.Default.CheckCircle else Icons.Default.Warning,
                    text = if (isComplete) "Perfil completo" else "$completionPercent% completo",
                    tint = if (isComplete) Color(0xFFBBF7D0) else AmarrilloSuave
                )
            }
        }
    }
}

@Composable
private fun HeroPill(
    icon: ImageVector,
    text: String,
    tint: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.16f))
            .padding(horizontal = 13.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(14.dp)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun QuickBusinessStatus(
    isComplete: Boolean,
    missingFields: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(if (isComplete) Color(0xFFECFDF5) else WarningSoft)
            .border(
                width = 1.dp,
                color = if (isComplete) Success.copy(alpha = 0.22f) else WarningColor.copy(alpha = 0.22f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(if (isComplete) Success.copy(alpha = 0.12f) else WarningColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isComplete) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isComplete) Success else WarningColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isComplete) "Tu negocio está completo" else "Faltan datos por completar",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = if (isComplete) {
                    "La información principal ya está lista."
                } else {
                    "Completa $missingFields dato${if (missingFields != 1) "s" else ""} para mejorar tu perfil."
                },
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

private data class BusinessField(
    val icon: ImageVector,
    val label: String,
    val value: String?
)

@Composable
private fun GroupedInfoCard(
    fields: List<BusinessField>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.80f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        fields.forEachIndexed { index, item ->
            val completed = !item.value.isNullOrBlank()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(39.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (completed) MaterialTheme.colorScheme.primary.copy(alpha = 0.09f)
                            else WarningColor.copy(alpha = 0.10f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (completed) MaterialTheme.colorScheme.primary else WarningColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(13.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = item.value?.takeIf { it.isNotBlank() } ?: "Sin completar",
                        fontSize = 14.sp,
                        color = if (completed) TextPrimary else WarningColor,
                        fontWeight = if (completed) FontWeight.SemiBold else FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (completed) Success else WarningColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (index < fields.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 17.dp),
                    thickness = 0.8.dp,
                    color = BorderGray.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun MiniBusinessUpgradeBanner(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0F172A),
                        GrisModerno
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                navController.navigate(ClientScreen.Planes.route)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(AmarrilloSuave.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = AmarrilloSuave,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Impulsa tu negocio con PRO",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "Más estadísticas, recibos y colaboradores.",
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = null,
            tint = AmarrilloSuave,
            modifier = Modifier.size(20.dp)
        )
    }
}

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
                elevation = 3.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.8f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(AmarrilloSuave.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = AmarrilloSuave,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = planName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = planExplanation,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(AmarrilloSuave.copy(alpha = 0.16f))
                    .padding(horizontal = 11.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Bs. $planPrice",
                    color = Color(0xFF92400E),
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(17.dp))

        HorizontalDivider(
            thickness = 0.8.dp,
            color = BorderGray.copy(alpha = 0.85f)
        )

        Spacer(Modifier.height(14.dp))

        PlanFeatureRow(
            icon = Icons.Default.Group,
            text = "Hasta $maxCollab colaborador${if (maxCollab != 1) "es" else ""}"
        )

        Spacer(Modifier.height(8.dp))

        PlanFeatureRow(
            icon = Icons.Outlined.Storefront,
            text = "Herramientas para gestionar tu negocio"
        )

        Spacer(Modifier.height(18.dp))

        UpgradeBadge(
            text = "Ver planes PRO",
            navController = navController
        )
    }
}

@Composable
private fun PlanFeatureRow(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun UpgradeBadge(
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
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        Color(0xFFD81B60)
                    )
                )
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

@Composable
private fun SectionLabel(
    label: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = label.uppercase(),
        modifier = modifier,
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Black,
        color = TextSecondary.copy(alpha = 0.82f),
        letterSpacing = 1.1.sp
    )
}