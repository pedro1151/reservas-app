package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

private val PageBg      = Color(0xFF0A0F1A)   // fondo oscuro unificado con las cards
private val TextPrimary = Color(0xFFE2E8F0)
private val TextMuted   = Color(0xFF64748B)

@Composable
fun PlansScreen(navController: NavHostController) {

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {

        // ── Back ──────────────────────────────────────────────────────────
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 8.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver", tint = TextPrimary)
                }
            }
        }

        // ── Headline ──────────────────────────────────────────────────────
        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Planes",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = (-0.8).sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Elige el plan perfecto\npara tu negocio.",
                    fontSize = 15.sp,
                    color = TextMuted,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(32.dp))
            }
        }

        // ── Estándar ──────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                StandardPlanCard(
                    title    = "Estándar",
                    tagline  = "Todo lo que tu negocio necesita para arrancar fuerte.",
                    price    = "$8",
                    period   = "usd / mes",
                    label    = "✦ Más popular",
                    badge    = "Mejor relación calidad-precio",
                    features = listOf(
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
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                ProPlanCard(
                    title    = "Pro",
                    tagline  = "Más poder para crecer sin límites de equipo.",
                    price    = "$15",
                    period   = "usd / mes",
                    label    = "⚡ Para equipos",
                    badge    = "Ideal para negocios en crecimiento",
                    features = listOf(
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
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                GoldPlanCard(
                    title    = "Gold",
                    tagline  = "Escala sin límites. Para negocios grandes y ambiciosos.",
                    price    = "$35",
                    period   = "usd / mes",
                    label    = "♛ Premium",
                    badge    = "Multi negocio · Sin restricciones",
                    features = listOf(
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
        }
    }
}
