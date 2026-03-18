package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class PricingTier(
    val key: String,
    val label: String,
    val annualTotal: String,
    val annualPerMonth: String,
    val monthlyPrice: String,
    val annualSavings: String
)

private val pricingTiers = listOf(
    PricingTier("1",   "1 usuario",    "USD 99/año",  "USD 8,25/mes",  "USD 12,99/mes", "Ahorrar 36%"),
    PricingTier("2-4", "2-4 usuarios", "USD 179/año", "USD 14,92/mes", "USD 22,99/mes", "Ahorrar 35%"),
    PricingTier("5-6", "5-6 usuarios", "USD 239/año", "USD 19,92/mes", "USD 30,99/mes", "Ahorrar 36%")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProTrialBottomSheet(onDismiss: () -> Unit) {
    var selectedPlan  by remember { mutableStateOf("annual") }
    var selectedUsers by remember { mutableStateOf("2-4") }

    val tier = pricingTiers.first { it.key == selectedUsers }

    // fillMaxHeight(0.90f) fija la altura del sheet en todos los estados → sin scroll jump
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = Color.White,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle       = null,
        modifier         = Modifier.fillMaxHeight(0.90f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {

            // ── Drag handle ────────────────────────────────────────────────
            Box(
                Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.width(40.dp).height(4.dp).background(Color(0xFFDDDDDD), CircleShape))
            }

            // ── Cerrar ─────────────────────────────────────────────────────
            Box(Modifier.fillMaxWidth().padding(end = 16.dp, top = 4.dp)) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF2F2F2))
                        .clickable(remember { MutableInteractionSource() }, null, onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, null, tint = Color(0xFF555555), modifier = Modifier.size(17.dp))
                }
            }

            Spacer(Modifier.height(6.dp))

            // ── Badge PRO ──────────────────────────────────────────────────
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF111111)) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(12.dp))
                        Text("PRO", fontSize = 11.sp, fontWeight = FontWeight.W800, color = Color.White, letterSpacing = 1.5.sp)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Título ─────────────────────────────────────────────────────
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Comienza tu prueba\ngratuita de 14 días",
                    fontSize = 24.sp, fontWeight = FontWeight.W800,
                    color = Color(0xFF111111), textAlign = TextAlign.Center, lineHeight = 30.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Accede a todas las funciones Pro sin costo.\nCancela cuando quieras.",
                    fontSize = 14.sp, color = Color(0xFF9E9E9E),
                    textAlign = TextAlign.Center, lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(18.dp))

            // ── Feature pills ──────────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeaturePill(Icons.Outlined.Notifications, "Recordatorios")
                FeaturePill(Icons.Outlined.Analytics,     "Reportes")
                FeaturePill(Icons.Outlined.Palette,       "Temas")
            }

            Spacer(Modifier.height(22.dp))

            // ── Tabs usuarios — altura fija en todos los tabs ──────────────
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(4.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    pricingTiers.forEach { t ->
                        val isSel = selectedUsers == t.key
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)          // ← altura fija: no hay reflow
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) Color.White else Color.Transparent)
                                .then(
                                    if (isSel) Modifier.border(0.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
                                    else Modifier
                                )
                                .clickable(remember { MutableInteractionSource() }, null) { selectedUsers = t.key },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                t.label,
                                fontSize   = 11.sp,
                                fontWeight = if (isSel) FontWeight.W700 else FontWeight.W400,
                                color      = if (isSel) Color(0xFF111111) else Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Plan cards — altura fija → sin reflow al cambiar tier ──────
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PlanCard(
                    isSelected = selectedPlan == "annual",
                    title      = "Anual",
                    subtitle   = "${tier.annualPerMonth}  ·  total ${tier.annualTotal}",
                    badge      = tier.annualSavings,
                    isPopular  = true,
                    onClick    = { selectedPlan = "annual" }
                )
                PlanCard(
                    isSelected = selectedPlan == "monthly",
                    title      = "Mensual",
                    subtitle   = tier.monthlyPrice,
                    badge      = null,
                    isPopular  = false,
                    onClick    = { selectedPlan = "monthly" }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Resumen de precio ──────────────────────────────────────────
            Surface(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF9F9F9)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PriceRow("Hoy  (14 días gratis)", "USD 0", bold = false, valueColor = Color(0xFF2E7D32))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    PriceRow(
                        "Después de la prueba",
                        if (selectedPlan == "annual") tier.annualTotal else tier.monthlyPrice,
                        bold = true
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── CTA ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFF1A1A1A), Color(0xFF444444))))
                    .clickable(remember { MutableInteractionSource() }, null, onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.FlightTakeoff, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Iniciar prueba gratuita", fontSize = 16.sp, fontWeight = FontWeight.W700, color = Color.White)
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Legal ──────────────────────────────────────────────────────
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Se renueva automáticamente. Cancela en cualquier momento.",
                    fontSize = 11.sp, color = Color(0xFFBBBBBB),
                    textAlign = TextAlign.Center, lineHeight = 15.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Más detalles",           fontSize = 11.sp, color = Color(0xFF757575), textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { })
                    Text("Términos y condiciones", fontSize = 11.sp, color = Color(0xFF757575), textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { })
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(icon: ImageVector, label: String) {
    Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFF5F5F5)) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, null, tint = Color(0xFF555555), modifier = Modifier.size(13.dp))
            Text(label, fontSize = 11.sp, color = Color(0xFF555555), fontWeight = FontWeight.W500)
        }
    }
}

@Composable
private fun PlanCard(
    isSelected: Boolean,
    title: String,
    subtitle: String,
    badge: String?,
    isPopular: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(if (isSelected) Color(0xFF111111) else Color(0xFFE0E0E0), tween(200), label = "b")
    val bgColor     by animateColorAsState(if (isSelected) Color(0xFFF8F8F8) else Color.White,       tween(200), label = "bg")

    Surface(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(72.dp),   // ← altura fija
        shape    = RoundedCornerShape(14.dp),
        color    = bgColor,
        border   = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // Radio
            Box(
                Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF111111) else Color.Transparent)
                    .border(if (isSelected) 0.dp else 2.dp, if (isSelected) Color.Transparent else Color(0xFFCCCCCC), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) Box(Modifier.size(8.dp).background(Color.White, CircleShape))
            }

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color(0xFF111111))
                    if (isPopular) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF111111)) {
                            Text("Popular", fontSize = 9.sp, fontWeight = FontWeight.W700, color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF9E9E9E), maxLines = 1)
            }

            // Badge o spacer invisible para mantener layout estable
            Box(Modifier.width(80.dp), contentAlignment = Alignment.CenterEnd) {
                if (badge != null) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFFF8E1)) {
                        Text(badge, Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp, fontWeight = FontWeight.W700, color = Color(0xFFD97706))
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, bold: Boolean, valueColor: Color = Color(0xFF111111)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 13.sp, color = if (bold) Color(0xFF333333) else Color(0xFF9E9E9E), fontWeight = if (bold) FontWeight.W600 else FontWeight.W400)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.W700, color = valueColor)
    }
}
