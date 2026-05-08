package com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplan

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.planes.PlanType
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.googleplaybilling.GooglePlayBillingViewModel
import com.optic.pramozventicoappz.presentation.ui.theme.GradientBackground

@Composable
fun PlansScreen(
    navController: NavHostController,
    googlePlayBillingViewModel: GooglePlayBillingViewModel
) {

    var headerVisible by remember { mutableStateOf(false) }

    val headerAlpha by animateFloatAsState(
        targetValue = if (headerVisible) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "hA"
    )

    val headerSlide by animateFloatAsState(
        targetValue = if (headerVisible) 0f else -20f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "hS"
    )

    LaunchedEffect(Unit) {
        headerVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {

        PlansBackground()

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.16f))
                        .padding(top = 8.dp, start = 6.dp, end = 12.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            navController.navigate(ClientScreen.Sales.route) {
                                popUpTo(0)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = headerAlpha
                            translationY = headerSlide
                        }
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.10f))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.20f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Precios simples y transparentes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB8FFCD),
                            letterSpacing = 0.4.sp
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "Planes",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = (-0.8).sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Elige el plan perfecto\npara tu negocio.",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.86f),
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.8).sp
                    )

                    Spacer(Modifier.height(28.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TrustBadge("✓  Sin contratos")
                        Spacer(Modifier.width(8.dp))
                        DividerDot()
                        Spacer(Modifier.width(8.dp))
                        TrustBadge("✓  Cancela gratis")
                        Spacer(Modifier.width(8.dp))
                        DividerDot()
                        Spacer(Modifier.width(8.dp))
                        TrustBadge("✓  Soporte real")
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                    StandardPlanCard(
                        title = "Estándar",
                        tagline = "Todo lo que tu negocio necesita para arrancar fuerte.",
                        price = "$8",
                        period = "usd / mes",
                        label = "MÁS POPULAR",
                        badge = "Mejor relación calidad-precio",
                        animDelay = 80,
                        features = listOf(
                            "Estadísticas completas",
                            "Comparar meses, años y semanas",
                            "Hasta 5 colaboradores",
                            "Recibos personalizados con tu marca",
                            "Exportar a Excel y PDF",
                            "Historial ilimitado",
                            "Filtros por cliente y producto"
                        ),
                        onSelect = {
                            googlePlayBillingViewModel.selectPlan(PlanType.STANDARD)
                            navController.navigate(ClientScreen.PlanMode.route)
                        }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                    ProPlanCard(
                        title = "Pro",
                        tagline = "Más poder para crecer sin límites de equipo.",
                        price = "$15",
                        period = "usd / mes",
                        label = "PARA EQUIPOS",
                        badge = "Ideal para negocios en crecimiento",
                        animDelay = 180,
                        features = listOf(
                            "Todo en Estándar",
                            "Dashboard avanzado y top clientes",
                            "Hasta 10 colaboradores",
                            "Envío de recibos por WhatsApp",
                            "Recordatorios automáticos",
                            "Más estilos de recibos",
                            "Soporte prioritario"
                        ),
                        onSelect = {
                            googlePlayBillingViewModel.selectPlan(PlanType.PRO)
                            navController.navigate(ClientScreen.PlanMode.route)
                        }
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                    GoldPlanCard(
                        title = "Gold",
                        tagline = "Escala sin límites. Para negocios grandes y ambiciosos.",
                        price = "$35",
                        period = "usd / mes",
                        label = "PREMIUM",
                        badge = "Multi negocio · Sin restricciones",
                        animDelay = 280,
                        features = listOf(
                            "Todo en Pro",
                            "Estadísticas profesionales y Analytics",
                            "Hasta 20 colaboradores",
                            "Backup automático en la nube",
                            "Multi negocio desde una cuenta",
                            "Reportes inteligentes con IA",
                            "Modos Fire / Fintech / Analytics"
                        ),
                        onSelect = {
                            googlePlayBillingViewModel.selectPlan(PlanType.GOLD)
                            navController.navigate(ClientScreen.PlanMode.route)
                        }
                    )
                }
                Spacer(Modifier.height(32.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(GradientBackground)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🔒  Pago 100% seguro",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Todos los planes incluyen 7 días de prueba gratuita.\nSi no estás satisfecho, te devolvemos tu dinero.",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.90f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlansBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF141414),
                        Color(0xFF0E1F18),
                        Color(0xFF07130D)
                    )
                )
            )
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00D66F).copy(alpha = 0.34f),
                            Color.Transparent
                        ),
                        radius = size.width * 0.78f
                    ),
                    center = center.copy(
                        x = size.width * 0.12f,
                        y = size.height * 0.10f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF4B3E).copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        radius = size.width * 0.80f
                    ),
                    center = center.copy(
                        x = size.width * 1.05f,
                        y = size.height * 0.30f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF39FF88).copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        radius = size.width * 0.95f
                    ),
                    center = center.copy(
                        x = size.width * 0.50f,
                        y = size.height * 0.95f
                    )
                )
            }
    )
}

@Composable
private fun DividerDot() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(12.dp)
            .background(Color.White.copy(alpha = 0.35f))
    )
}

@Composable
private fun TrustBadge(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        color = Color.White.copy(alpha = 0.88f),
        fontWeight = FontWeight.Medium
    )
}