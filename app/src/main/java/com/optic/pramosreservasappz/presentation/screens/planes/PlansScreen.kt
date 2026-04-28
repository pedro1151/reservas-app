package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun PlansScreen(navController: NavHostController) {

    // Header animation
    var headerVisible by remember { mutableStateOf(false) }
    val headerAlpha by animateFloatAsState(
        targetValue   = if (headerVisible) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "hA"
    )
    val headerSlide by animateFloatAsState(
        targetValue   = if (headerVisible) 0f else -20f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "hS"
    )
    LaunchedEffect(Unit) { headerVisible = true }

    LazyColumn(
        state           = rememberLazyListState(),
        modifier        = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {

        // ── Navigation bar ────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PageBg)
                    .padding(top = 8.dp, start = 6.dp, end = 20.dp, bottom = 4.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // ── Hero header ───────────────────────────────────────────────────
        item {
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = headerAlpha; translationY = headerSlide }
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Eyebrow label
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(listOf(CyanAccent.copy(0.12f), IndigoMain.copy(0.10f)))
                        )
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(CyanAccent.copy(0.35f), IndigoMain.copy(0.25f))),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text          = "Precios simples y transparentes",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = CyanDeep,
                        letterSpacing = 0.4.sp
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Main headline
                Text(
                    "Planes",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    letterSpacing = (-0.8).sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Elige el plan perfecto\npara tu negocio.",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.8).sp
                )

                Spacer(Modifier.height(28.dp))

                // Trust indicators row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically,
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    TrustBadge("✓  Sin contratos")
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(12.dp)
                            .background(Ink200)
                    )
                    Spacer(Modifier.width(8.dp))
                    TrustBadge("✓  Cancela gratis")
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(12.dp)
                            .background(Ink200)
                    )
                    Spacer(Modifier.width(8.dp))
                    TrustBadge("✓  Soporte real")
                }
            }
        }

        // ── Estándar ──────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                StandardPlanCard(
                    title     = "Estándar",
                    tagline   = "Todo lo que tu negocio necesita para arrancar fuerte.",
                    price     = "$8",
                    period    = "usd / mes",
                    label     = "MÁS POPULAR",
                    badge     = "Mejor relación calidad-precio",
                    animDelay = 80,
                    features  = listOf(
                        "Estadísticas completas",
                        "Comparar meses, años y semanas",
                        "Hasta 5 colaboradores",
                        "Recibos personalizados con tu marca",
                        "Exportar a Excel y PDF",
                        "Historial ilimitado",
                        "Filtros por cliente y producto"
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // ── Pro ───────────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                ProPlanCard(
                    title     = "Pro",
                    tagline   = "Más poder para crecer sin límites de equipo.",
                    price     = "$15",
                    period    = "usd / mes",
                    label     = "PARA EQUIPOS",
                    badge     = "Ideal para negocios en crecimiento",
                    animDelay = 180,
                    features  = listOf(
                        "Todo en Estándar",
                        "Dashboard avanzado y top clientes",
                        "Hasta 10 colaboradores",
                        "Envío de recibos por WhatsApp",
                        "Recordatorios automáticos",
                        "Más estilos de recibos",
                        "Soporte prioritario"
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // ── Gold ──────────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                GoldPlanCard(
                    title     = "Gold",
                    tagline   = "Escala sin límites. Para negocios grandes y ambiciosos.",
                    price     = "$35",
                    period    = "usd / mes",
                    label     = "PREMIUM",
                    badge     = "Multi negocio · Sin restricciones",
                    animDelay = 280,
                    features  = listOf(
                        "Todo en Pro",
                        "Estadísticas profesionales y Analytics",
                        "Hasta 20 colaboradores",
                        "Backup automático en la nube",
                        "Multi negocio desde una cuenta",
                        "Reportes inteligentes con IA",
                        "Modos Fire / Fintech / Analytics"
                    )
                )
            }
            Spacer(Modifier.height(32.dp))
        }

        // ── FAQ / Garantía footer ─────────────────────────────────────────
        item {
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(PureWhite)
                        .border(1.dp, Ink200, RoundedCornerShape(20.dp))
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text       = "🔒  Pago 100% seguro",
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Ink900
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text      = "Todos los planes incluyen 7 días de prueba gratuita.\nSi no estás satisfecho, te devolvemos tu dinero.",
                            fontSize  = 13.sp,
                            color     = Ink400,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

// ─── Trust badge helper ───────────────────────────────────────────────────────
@Composable
private fun TrustBadge(text: String) {
    Text(
        text       = text,
        fontSize   = 11.sp,
        color      = Ink600,
        fontWeight = FontWeight.Medium
    )
}
