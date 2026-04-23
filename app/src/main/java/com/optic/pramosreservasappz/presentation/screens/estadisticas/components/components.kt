package com.optic.pramosreservasappz.presentation.screens.estadisticas.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.presentation.sales.Components.ReportEntry
import com.optic.pramosreservasappz.presentation.sales.Components.SAccent
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400

@Composable
fun RevenueBarChart(data: List<ReportEntry>, modifier: Modifier = Modifier) {
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
fun StatCard(label: String, value: String, sub: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, modifier: Modifier = Modifier) {
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