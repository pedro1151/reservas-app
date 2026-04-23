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

/* ─────────────────────────────────────────────── */
/* PALETA NUEVA                                   */
/* ─────────────────────────────────────────────── */

private val Pink = Color(0xFFE91E63)
private val PinkDark = Color(0xFFC2185B)
private val PinkSoft = Color(0xFFFCE4EC)

private val Gold = Color(0xFFFFB300)
private val GoldSoft = Color(0xFFFFF3CD)

private val Bg = Color(0xFFF8F4F6)
private val CardBg = Color(0xFFFFFFFF)
private val DarkSurface = Color(0xFF3A3A3A)

private val TextMain = Color(0xFF1B1B1B)
private val TextSoft = Color(0xFF6B7280)
private val Border = Color(0xFFE5E7EB)

/* ─────────────────────────────────────────────── */
/* SCREEN                                         */
/* ─────────────────────────────────────────────── */

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

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CircularProgressIndicator(
                        color = Pink,
                        strokeWidth = 2.8.dp
                    )

                    Text(
                        text = "Cargando negocio...",
                        color = TextSoft,
                        fontSize = 13.sp
                    )
                }
            }

            is Resource.Failure -> {

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Pink,
                        modifier = Modifier.size(34.dp)
                    )

                    Text(
                        text = "Error al cargar negocio",
                        color = TextMain,
                        fontSize = 14.sp
                    )
                }
            }

            is Resource.Success -> {

                val business =
                    (businessState as Resource.Success).data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = 42.dp
                    )
                ) {

                    /* HERO */
                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            PinkDark,
                                            Pink
                                        )
                                    )
                                )
                                .padding(
                                    horizontal = 22.dp,
                                    vertical = 28.dp
                                )
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(
                                        x = 24.dp,
                                        y = (-20).dp
                                    )
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(
                                            alpha = 0.08f
                                        )
                                    )
                            )

                            Column {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment =
                                    Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(58.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Color.White.copy(
                                                    alpha = 0.20f
                                                )
                                            )
                                            .border(
                                                1.6.dp,
                                                Color.White.copy(
                                                    alpha = 0.45f
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment =
                                        Alignment.Center
                                    ) {

                                        Text(
                                            text = business.name
                                                ?.take(2)
                                                ?.uppercase()
                                                ?: "MN",
                                            color = Color.White,
                                            fontWeight =
                                            FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    }

                                    Spacer(
                                        Modifier.width(14.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = "Mi negocio",
                                            color = Color.White.copy(
                                                alpha = 0.75f
                                            ),
                                            fontSize = 12.sp
                                        )

                                        Spacer(
                                            Modifier.height(2.dp)
                                        )

                                        Text(
                                            text = business.name
                                                ?: "Sin nombre",
                                            color = Color.White,
                                            fontWeight =
                                            FontWeight.Bold,
                                            fontSize = 21.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Color.White.copy(
                                                    alpha = 0.16f
                                                )
                                            )
                                            .clickable { },
                                        contentAlignment =
                                        Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector =
                                            Icons.Default.Edit,
                                            contentDescription =
                                            null,
                                            tint = Color.White,
                                            modifier =
                                            Modifier.size(
                                                18.dp
                                            )
                                        )
                                    }
                                }

                                Spacer(
                                    Modifier.height(18.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                50
                                            )
                                        )
                                        .background(
                                            Color.White.copy(
                                                alpha = 0.16f
                                            )
                                        )
                                        .padding(
                                            horizontal = 14.dp,
                                            vertical = 7.dp
                                        ),
                                    verticalAlignment =
                                    Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector =
                                        Icons.Default.Star,
                                        contentDescription =
                                        null,
                                        tint = Gold,
                                        modifier =
                                        Modifier.size(14.dp)
                                    )

                                    Spacer(
                                        Modifier.width(6.dp)
                                    )

                                    Text(
                                        text = business.plan.name,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight =
                                        FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    /* INFO */
                    item {
                        SectionLabel(
                            label = "Información del negocio",
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 16.dp
                            )
                        )
                    }

                    item {

                        GroupedInfoCard(
                            modifier = Modifier.padding(
                                horizontal = 14.dp
                            ),
                            fields = listOf(
                                Triple(
                                    Icons.Outlined.Email,
                                    "Email",
                                    business.email
                                ),
                                Triple(
                                    Icons.Outlined.Phone,
                                    "Teléfono",
                                    business.phone
                                ),
                                Triple(
                                    Icons.Outlined.Phone,
                                    "WhatsApp",
                                    business.whatsappNumber
                                ),
                                Triple(
                                    Icons.Outlined.LocationOn,
                                    "Ciudad",
                                    business.city
                                ),
                                Triple(
                                    Icons.Outlined.Public,
                                    "País",
                                    business.country
                                )
                            )
                        )
                    }

                    /* PLAN */
                    item {
                        SectionLabel(
                            label = "Tu plan activo",
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 16.dp
                            )
                        )
                    }

                    item {

                        PlanCard(
                            planName = business.plan.name,
                            planPrice =
                            business.plan.price.toString(),
                            planExplanation =
                            business.plan.explanation,
                            maxCollab =
                            business.plan.maxCollab,
                            navController =
                            navController,
                            modifier = Modifier.padding(
                                horizontal = 14.dp
                            )
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

/* ─────────────────────────────────────────────── */
/* LABEL                                          */
/* ─────────────────────────────────────────────── */

@Composable
private fun SectionLabel(
    label: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = label.uppercase(),
        modifier = modifier,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextSoft,
        letterSpacing = 1.1.sp
    )
}

/* ─────────────────────────────────────────────── */
/* INFO CARD                                      */
/* ─────────────────────────────────────────────── */

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
                shape = RoundedCornerShape(22.dp)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
    ) {

        fields.forEachIndexed { index, item ->

            val (icon, label, value) = item

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment =
                Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(PinkSoft),
                    contentAlignment =
                    Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Pink,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = TextSoft
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = value ?: "Sin completar",
                        fontSize = 14.sp,
                        color = if (value != null)
                            TextMain else TextSoft,
                        fontWeight =
                        FontWeight.Medium
                    )
                }
            }

            if (index < fields.lastIndex) {

                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 18.dp
                    ),
                    thickness = 0.8.dp,
                    color = Border
                )
            }
        }
    }
}

/* ─────────────────────────────────────────────── */
/* PLAN CARD                                      */
/* ─────────────────────────────────────────────── */

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
                shape = RoundedCornerShape(22.dp)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
            Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = planName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = planExplanation,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = TextSoft
                )
            }

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(50)
                    )
                    .background(GoldSoft)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = "\$$planPrice",
                    color = Gold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        HorizontalDivider(
            thickness = 0.8.dp,
            color = Border
        )

        Spacer(Modifier.height(14.dp))

        Row(
            verticalAlignment =
            Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = Pink,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(7.dp))

            Text(
                text =
                "Hasta $maxCollab colaborador${if (maxCollab != 1) "es" else ""}",
                fontSize = 13.sp,
                color = TextMain
            )
        }

        Spacer(Modifier.height(18.dp))

        UpgradeBadge(
            text = "Mejorar plan →",
            navController = navController
        )
    }
}

/* ─────────────────────────────────────────────── */
/* CTA                                            */
/* ─────────────────────────────────────────────── */

@Composable
fun UpgradeBadge(
    text: String,
    navController: NavHostController
) {

    val interaction =
        remember { MutableInteractionSource() }

    val pressed by
    interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue =
        if (pressed) 0.97f else 1f,
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
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Pink,
                        PinkDark
                    )
                )
            )
            .clickable(
                interactionSource =
                interaction,
                indication = null
            ) {
                navController.navigate(
                    ClientScreen.Planes.route
                )
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