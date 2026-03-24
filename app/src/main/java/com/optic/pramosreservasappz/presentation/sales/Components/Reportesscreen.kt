package com.optic.pramosreservasappz.presentation.sales.Components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

@Composable
fun ReportesScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var selectedPeriod by remember { mutableStateOf(1) }
    val periods = listOf("Hoy", "Semana", "Mes", "Año")

    val chartData = remember(selectedPeriod) {
        when (selectedPeriod) {
            0    -> listOf(ReportEntry("AM", 45.0), ReportEntry("MD", 85.0), ReportEntry("PM", 130.0, isToday = true))
            1    -> fakeWeekReport
            2    -> fakeMonthReport.takeLast(14)
            else -> listOf(ReportEntry("Ene", 920.0), ReportEntry("Feb", 1100.0), ReportEntry("Mar", 850.0, isToday = true))
        }
    }
    val totalRevenue = chartData.sumOf { it.value }
    val avgTicket    = fakeSales.filter { it.status == SaleStatus.COMPLETED }.let { if (it.isEmpty()) 0.0 else it.sumOf { s -> s.total } / it.size }
    val totalCount   = fakeSales.count { it.status == SaleStatus.COMPLETED }
    val cancelCount  = fakeSales.count { it.status == SaleStatus.CANCELLED }
    val topServices  = fakeSales.filter { it.status == SaleStatus.COMPLETED }
        .flatMap { sale -> sale.items.map { item -> item to sale.total / sale.items.size } }
        .groupBy { it.first }.mapValues { entry -> entry.value.sumOf { it.second } }
        .entries.sortedByDescending { it.value }.take(5)

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 40.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF111111), Color(0xFF1A1A1A)))).padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 28.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f)).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onBack), contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Text("REPORTES", fontSize = 12.sp, fontWeight = FontWeight.W700, color = SAccent, letterSpacing = 1.5.sp)
                            Spacer(Modifier.size(36.dp))
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("Bs ${"%.2f".format(totalRevenue)}", fontSize = 36.sp, fontWeight = FontWeight.W800, color = Color.White, letterSpacing = (-1).sp)
                        Text("ingresos · ${periods[selectedPeriod].lowercase()}", fontSize = 13.sp, color = Color(0xFF777777))
                        Spacer(Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.06f)).padding(4.dp)) {
                            periods.forEachIndexed { index, label ->
                                Box(
                                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                                        .background(if (selectedPeriod == index) Color.White.copy(alpha = 0.12f) else Color.Transparent)
                                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { selectedPeriod = index }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(label, fontSize = 13.sp, fontWeight = if (selectedPeriod == index) FontWeight.W700 else FontWeight.W400, color = if (selectedPeriod == index) Color.White else Color(0xFF888888))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Ingresos", fontSize = 15.sp, fontWeight = FontWeight.W700, color = SBlack)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(Modifier.size(8.dp).clip(CircleShape).background(SAccent))
                                Text("Completadas", fontSize = 11.sp, color = SGray400)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        AnimatedContent(targetState = selectedPeriod, transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) }, label = "chart_anim") { _ ->
                            RevenueBarChart(data = chartData, modifier = Modifier.fillMaxWidth().height(140.dp))
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard("Ventas",     totalCount.toString(),                   "completadas", Icons.Outlined.ShoppingBag, SAccent,             Modifier.weight(1f))
                    StatCard("Ticket",     "Bs ${"%.0f".format(avgTicket)}",        "promedio",    Icons.Outlined.Receipt,     Color(0xFF5C6BC0),    Modifier.weight(1f))
                    StatCard("Canceladas", cancelCount.toString(),                  "ventas",      Icons.Outlined.Cancel,      SRed,                 Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        Text("Servicios más vendidos", fontSize = 15.sp, fontWeight = FontWeight.W700, color = SBlack)
                        Spacer(Modifier.height(16.dp))
                        val maxVal = topServices.maxOfOrNull { it.value } ?: 1.0
                        topServices.forEachIndexed { index, (name, total) ->
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                                        Text("${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.W700, color = SGray400, modifier = Modifier.width(16.dp))
                                        Text(name, fontSize = 13.sp, fontWeight = FontWeight.W500, color = SBlack)
                                    }
                                    Text("Bs ${"%.0f".format(total)}", fontSize = 13.sp, fontWeight = FontWeight.W700, color = SBlack)
                                }
                                Spacer(Modifier.height(6.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(SGray100)) {
                                    val barColor = listOf(SAccent, Color(0xFF5C6BC0), Color(0xFFFF7043), Color(0xFFEC407A), Color(0xFF26A69A))[index % 5]
                                    Box(modifier = Modifier.fillMaxWidth((total / maxVal).toFloat()).fillMaxHeight().clip(RoundedCornerShape(2.dp)).background(barColor))
                                }
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RevenueBarChart(data: List<ReportEntry>, modifier: Modifier = Modifier) {
    val accentColor = SAccent
    val grayColor   = Color(0xFFE8E8E8)
    val maxVal      = remember(data) { data.maxOfOrNull { it.value }?.toFloat()?.coerceAtLeast(1f) ?: 1f }
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barCount  = data.size
            val slotWidth = size.width / barCount
            val barWidth  = (slotWidth * 0.45f).coerceAtMost(28.dp.toPx())
            val maxBarH   = size.height * 0.82f
            val cornerR   = 5.dp.toPx()
            data.forEachIndexed { i, entry ->
                val barH = ((entry.value.toFloat() / maxVal) * maxBarH).coerceAtLeast(4.dp.toPx())
                val x    = i * slotWidth + slotWidth / 2f - barWidth / 2f
                drawRoundRect(color = if (entry.isToday) accentColor else grayColor, topLeft = Offset(x, size.height - barH), size = Size(barWidth, barH), cornerRadius = CornerRadius(cornerR, cornerR))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceEvenly) {
            data.forEach { entry ->
                Text(entry.label, fontSize = 10.sp, color = if (entry.isToday) SAccent else SGray400, fontWeight = if (entry.isToday) FontWeight.W700 else FontWeight.W400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, sub: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color.White).padding(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(iconColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(16.dp))
            }
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.W800, color = SBlack, letterSpacing = (-0.3).sp)
            Text("$label\n$sub", fontSize = 10.sp, color = SGray400, lineHeight = 14.sp)
        }
    }
}