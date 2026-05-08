package com.optic.pramozventicoappz.presentation.screens.inicio.components

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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramozventicoappz.presentation.sales.Components.SAccent
import com.optic.pramozventicoappz.presentation.sales.Components.SBlack
import com.optic.pramozventicoappz.presentation.sales.Components.SGray200
import com.optic.pramozventicoappz.presentation.sales.Components.SGray400
import com.optic.pramozventicoappz.presentation.sales.Components.SaleStatus
import com.optic.pramozventicoappz.presentation.sales.Components.fakeSales
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RecibosScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val completedSales = remember {
        fakeSales.filter { it.status == SaleStatus.COMPLETED }
            .sortedByDescending { it.date.toEpochDay() * 100000 + it.time.toSecondOfDay() }
    }
    var expandedId     by remember { mutableStateOf<Int?>(null) }
    var showShareToast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showShareToast) {
        if (showShareToast != null) { kotlinx.coroutines.delay(2000); showShareToast = null }
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 40.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF111111), Color(0xFF1A1A1A)))).padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 28.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f)).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onBack), contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Text("RECIBOS", fontSize = 12.sp, fontWeight = FontWeight.W700, color = SAccent, letterSpacing = 1.5.sp)
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.FilterList, null, tint = Color(0xFF888888), modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("${completedSales.size} recibos", fontSize = 32.sp, fontWeight = FontWeight.W800, color = Color.White, letterSpacing = (-1).sp)
                        Text("ventas completadas", fontSize = 13.sp, color = Color(0xFF777777))
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).clip(RoundedCornerShape(12.dp)).background(Color.White).border(1.dp, SGray200, RoundedCornerShape(12.dp)).padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Outlined.Search, null, tint = SGray400, modifier = Modifier.size(18.dp))
                    Text("Buscar por cliente o servicio…", fontSize = 14.sp, color = SGray400)
                }
            }

            items(completedSales, key = { "recibo_${it.id}" }) { sale ->
                val isExpanded = expandedId == sale.id
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { expandedId = if (isExpanded) null else sale.id }
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(
                                SAccent.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Receipt, null, tint = SAccent, modifier = Modifier.size(20.dp))
                            }
                            Column(Modifier.weight(1f)) {
                                Text(sale.clientName, fontSize = 14.sp, fontWeight = FontWeight.W600, color = SBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(sale.items.joinToString(", "), fontSize = 12.sp, color = SGray400, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("Bs ${"%.2f".format(sale.total)}", fontSize = 14.sp, fontWeight = FontWeight.W700, color = SBlack)
                                Text(sale.date.format(DateTimeFormatter.ofPattern("d MMM", Locale("es","ES"))), fontSize = 11.sp, color = SGray400)
                            }
                            Icon(if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = SGray400, modifier = Modifier.size(18.dp))
                        }

                        AnimatedVisibility(visible = isExpanded, enter = expandVertically(tween(280, easing = EaseOutCubic)) + fadeIn(tween(220)), exit = shrinkVertically(tween(220, easing = EaseInCubic)) + fadeOut(tween(180))) {
                            Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9)).padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("N° Recibo", fontSize = 12.sp, color = SGray400)
                                    Text("#REC-${1000 + sale.id}", fontSize = 12.sp, fontWeight = FontWeight.W600, color = SBlack)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Fecha", fontSize = 12.sp, color = SGray400)
                                    Text(sale.date.format(DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale("es","ES"))), fontSize = 12.sp, fontWeight = FontWeight.W600, color = SBlack)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Hora", fontSize = 12.sp, color = SGray400)
                                    Text(sale.time.format(DateTimeFormatter.ofPattern("HH:mm")), fontSize = 12.sp, fontWeight = FontWeight.W600, color = SBlack)
                                }
                                HorizontalDivider(color = SGray200, modifier = Modifier.padding(vertical = 4.dp))
                                sale.items.forEach { item ->
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(item, fontSize = 13.sp, color = SBlack)
                                        Text("Bs ${"%.2f".format(sale.total / sale.items.size)}", fontSize = 13.sp, fontWeight = FontWeight.W600, color = SBlack)
                                    }
                                }
                                HorizontalDivider(color = SGray200, modifier = Modifier.padding(vertical = 4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("TOTAL", fontSize = 13.sp, fontWeight = FontWeight.W700, color = SBlack)
                                    Text("Bs ${"%.2f".format(sale.total)}", fontSize = 14.sp, fontWeight = FontWeight.W800, color = SAccent)
                                }
                                Spacer(Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ReceiptActionButton(Icons.Outlined.Share,    "Compartir", SBlack,             Modifier.weight(1f)) { showShareToast = "Recibo #REC-${1000 + sale.id}" }
                                    ReceiptActionButton(Icons.Outlined.Download, "Descargar", Color(0xFF5C6BC0),  Modifier.weight(1f)) { showShareToast = "Descargando…" }
                                    ReceiptActionButton(Icons.Outlined.Send,     "Enviar",    Color(0xFF25D366),  Modifier.weight(1f)) { showShareToast = "Enviando…" }
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = showShareToast != null, enter = slideInVertically(tween(280)) { -it } + fadeIn(tween(200)), exit = slideOutVertically(tween(240)) { -it } + fadeOut(tween(180)), modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(24.dp)).background(SBlack).padding(horizontal = 20.dp, vertical = 10.dp)) {
                Text(showShareToast ?: "", fontSize = 13.sp, fontWeight = FontWeight.W600, color = Color.White)
            }
        }
    }
}

@Composable
private fun ReceiptActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.08f)).border(1.dp, color.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick).padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.W600, color = color)
        }
    }
}