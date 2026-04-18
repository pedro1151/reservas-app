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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.business.abmmember.components.MemberInputCard
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel
import com.optic.pramosreservasappz.presentation.screens.business.mybusiness.components.BusinessInfoRow
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun MyBusinessContent(
    paddingValues: PaddingValues,
    viewModel: BusinessViewModel,
    navController: NavHostController
) {

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Cyan.copy(alpha = 0.10f)

    val Graphite = Color(0xFF0D0D0D)
    val GrayDark = Color(0xFF374151)
    val GraySoft = Color(0xFFF3F4F6)

    val businessState by viewModel.businessState

    LaunchedEffect(Unit) {
        viewModel.getBusinessById(1, 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftCoolBackground)
            .padding(paddingValues)
    ) {

        when (businessState) {

            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Cyan
                )
            }

            is Resource.Failure -> {
                Text(
                    "Error al cargar negocio",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }

            is Resource.Success -> {

                val business = (businessState as Resource.Success).data

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {

                    // 🔥 HEADER CARD (más pro)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    14.dp,
                                    RoundedCornerShape(24.dp),
                                    ambientColor = Color.Black.copy(0.06f),
                                    spotColor = Color.Black.copy(0.1f)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.White)
                                    .padding(20.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {
                                        Text(
                                            "Mi negocio",
                                            fontSize = 13.sp,
                                            color = GrayDark
                                        )

                                        Spacer(Modifier.height(4.dp))

                                        Text(
                                            business.name ?: "Sin completar",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Graphite
                                        )
                                    }

                                    IconButton(onClick = { }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = Cyan
                                        )
                                    }
                                }

                                Spacer(Modifier.height(14.dp))

                                // 🔥 BADGE PLAN
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(CyanSoft)
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Cyan,
                                        modifier = Modifier.size(14.dp)
                                    )

                                    Spacer(Modifier.width(6.dp))

                                    Text(
                                        business.plan.name,
                                        color = Cyan,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // 🔥 INFO SECTION (estilo MercadoPago)
                    item {

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            BusinessField("Email", business.email)
                            BusinessField("Teléfono", business.phone)
                            BusinessField("WhatsApp", business.whatsappNumber)
                            BusinessField("Ciudad", business.city)
                            BusinessField("País", business.country)
                        }
                    }

                    // 🔥 PLAN SECTION (más potente)
                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    10.dp,
                                    RoundedCornerShape(20.dp),
                                    ambientColor = Color.Black.copy(0.05f),
                                    spotColor = Color.Black.copy(0.08f)
                                )
                        ) {

                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(18.dp)
                            ) {

                                Text(
                                    "Tu plan",
                                    fontSize = 14.sp,
                                    color = GrayDark
                                )

                                Spacer(Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        business.plan.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Graphite
                                    )

                                    Text(
                                        "$${business.plan.price}",
                                        fontWeight = FontWeight.Bold,
                                        color = Cyan
                                    )
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    business.plan.explanation,
                                    fontSize = 13.sp,
                                    color = GrayDark
                                )

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "Hasta ${business.plan.maxCollab} colaboradores",
                                    fontSize = 12.sp,
                                    color = GrayDark
                                )

                                Spacer(Modifier.height(16.dp))

                                // 🔥 BOTÓN BADGE PRO (NO LINK)
                                UpgradeBadge(
                                    text = "Mejorar plan",
                                    color = Color(0xFF111827), // 🔥 contraste PRO
                                    contentColor = Color.White,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun BusinessField(
    label: String,
    value: String?
) {

    val Graphite = Color(0xFF0D0D0D)
    val GrayDark = Color(0xFF6B7280)
    val GraySoft = Color(0xFFF3F4F6)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(
                1.dp,
                GraySoft,
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {

        Text(
            text = label,
            fontSize = 11.sp,
            color = GrayDark
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = value ?: "Sin completar",
            fontSize = 15.sp,
            color = Graphite,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun UpgradeBadge(
    text: String,
    color: Color,
    contentColor: Color,
    navController: NavHostController
) {

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = ""
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(50))
            .background(color)
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
                navController.navigate(ClientScreen.Planes.route)
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}