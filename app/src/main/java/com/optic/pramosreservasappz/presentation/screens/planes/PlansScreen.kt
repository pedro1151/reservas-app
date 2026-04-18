package com.optic.pramosreservasappz.presentation.screens.planes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

private val PageBackground = Color(0xFFF0F2F5)
private val TextPrimary    = Color(0xFF0D1B2A)
private val TextMuted      = Color(0xFF64748B)
private val LabelMuted     = Color(0xFF94A3B8)

@Composable
fun PlansScreen(navController: NavHostController) {

    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {

        // ── Back ──────────────────────────────────────────────────────────
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 8.dp)) {
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
                .padding(horizontal = 26.dp)) {
                Spacer(Modifier.height(4.dp))
                Text("Planes", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary, letterSpacing = (-0.8).sp)
                Spacer(Modifier.height(6.dp))
                Text("Escoge el plan que mejor\nse adapta a tu negocio.",
                    fontSize = 15.sp, color = TextMuted, lineHeight = 22.sp)
                Spacer(Modifier.height(30.dp))
            }
        }

        // ── Estándar (featured) ───────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                FeaturedPlanCard(
                    title    = "Estándar",
                    subtitle = "Todo lo que tu negocio necesita para arrancar.",
                    price    = "$8",
                    period   = "usd / mes",
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
            Spacer(Modifier.height(14.dp))
        }

        // ── Section label ─────────────────────────────────────────────────
        item {
            Text(
                "Más opciones",
                fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                color = LabelMuted, letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 26.dp, top = 8.dp, bottom = 14.dp)
            )
        }

        // ── Pro ───────────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                ProPlanCard(
                    title    = "Pro",
                    tagline  = "Para equipos que quieren crecer más rápido.",
                    price    = "$15",
                    period   = "usd/mes",
                    features = listOf(
                        "Todo en Estándar",
                        "Dashboard avanzado y top clientes",
                        "Hasta 10 colaboradores",
                        "Recibos por WhatsApp",
                        "Recordatorios automáticos"
                    )
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        // ── Gold ──────────────────────────────────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                GoldPlanCard(
                    title    = "Gold",
                    tagline  = "Escala sin límites. Pensado para negocios grandes.",
                    price    = "$35",
                    period   = "usd/mes",
                    features = listOf(
                        "Todo en Pro",
                        "Estadísticas profesionales y Analytics",
                        "Hasta 20 colaboradores",
                        "Backup automático",
                        "Multi negocio y reportes inteligentes"
                    )
                )
            }
        }
    }
}
