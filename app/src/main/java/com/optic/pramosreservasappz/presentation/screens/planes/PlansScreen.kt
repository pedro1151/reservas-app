package com.optic.pramosreservasappz.presentation.screens.planes


import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno

@Composable
fun PlansScreen(
    navController: NavHostController
) {

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Cyan.copy(alpha = 0.12f)
    val Gold = Color(0xFFFFC857)

    val Background = Color(0xFFF8FAFC)
    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF64748B)
    val BorderSoft = Color(0xFFE2E8F0)

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    // tint = MaterialTheme.colorScheme.getGreenLima // MaterialTheme.colorScheme.onPrimary // Ícono blanco si fondo es primario
                )
            }
            Text(
                "Elige tu plan segun tus necesidades",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            PlanCard(
                title = "Estándar",
                price = "$ 8 usd / mes",
                highlight = true,
                badge = "RECOMENDADO",
                color = Cyan,
                features = listOf(
                    "Estadísticas completas",
                    "Comparar meses / años / semanas",
                    "Hasta 5 colaboradores",
                    "Recibos personalizados con tu marca",
                    "Exportar a Excel / PDF",
                    "Historial ilimitado",
                    "Filtros por cliente y producto"
                )
            )
        }

        item {
            PlanCard(
                title = "Pro",
                price = "$ 15 usd / mes",
                highlight = false,
                color = Cyan,
                features = listOf(
                    "Todo en Estándar",
                    "Dashboard avanzado",
                    "Top productos y clientes",
                    "Más estilos de recibos",
                    "Hasta 10 colaboradores",
                    "Envío de recibos por WhatsApp",
                    "Recordatorios automáticos"
                )
            )
        }

        item {
            PlanCard(
                title = "Gold",
                price = "$ 35 usd / mes",
                highlight = false,
                color = Gold,
                features = listOf(
                    "Todo en Pro",
                    "Estadísticas profesionales",
                    "Modos Fire / Fintech / Analytics",
                    "Hasta 20 colaboradores",
                    "Backup automático",
                    "Multi negocio",
                    "Reportes inteligentes"
                )
            )
        }

        item { Spacer(Modifier.height(40.dp)) }
    }
}