package com.optic.pramosreservasappz.presentation.screens.inicio.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
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
import com.optic.pramosreservasappz.presentation.sales.Components.ProductItem
import com.optic.pramosreservasappz.presentation.sales.Components.SAccent
import com.optic.pramosreservasappz.presentation.sales.Components.SAmber
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray100
import com.optic.pramosreservasappz.presentation.sales.Components.SGray200
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.sales.Components.fakeProducts

@Composable
fun StockScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val stockState  = remember { fakeProducts.map { it.copy() }.toMutableStateList() }
    val lowStock    = stockState.count { it.stock in 1..3 }
    val outOfStock  = stockState.count { it.stock == 0 }
    val totalUnits  = stockState.sumOf { it.stock }
    var showAddSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 40.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF111111), Color(0xFF1A1A1A)))).padding(horizontal = 20.dp).padding(top = 16.dp, bottom = 28.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f)).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onBack), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Text("INVENTARIO", fontSize = 12.sp, fontWeight = FontWeight.W700, color = SAccent, letterSpacing = 1.5.sp)
                            Spacer(Modifier.size(36.dp))
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("$totalUnits unidades", fontSize = 32.sp, fontWeight = FontWeight.W800, color = Color.White, letterSpacing = (-1).sp)
                        Text("en ${stockState.size} productos registrados", fontSize = 13.sp, color = Color(0xFF777777))
                        Spacer(Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (lowStock > 0)   StockAlertChip(Icons.Outlined.Warning,              "$lowStock stock bajo",  SAmber)
                            if (outOfStock > 0) StockAlertChip(Icons.Outlined.RemoveShoppingCart,   "$outOfStock sin stock", SRed)
                            if (lowStock == 0 && outOfStock == 0) StockAlertChip(Icons.Outlined.CheckCircle, "Todo en orden", SAccent)
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MiniStockCard("Productos", stockState.size.toString(), SGray600, Modifier.weight(1f))
                    MiniStockCard("Stock bajo", lowStock.toString(),       SAmber,   Modifier.weight(1f))
                    MiniStockCard("Sin stock",  outOfStock.toString(),     SRed,     Modifier.weight(1f))
                }
            }

            item { Text("PRODUCTOS", fontSize = 11.sp, fontWeight = FontWeight.W700, color = SGray400, letterSpacing = 0.8.sp, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) }

            itemsIndexed(stockState, key = { _, p -> "stock_${p.id}" }) { index, product ->
                StockProductRow(
                    product  = product,
                    onAdd    = { stockState[index] = product.copy(stock = product.stock + 1) },
                    onRemove = { if (product.stock > 0) stockState[index] = product.copy(stock = product.stock - 1) }
                )
                if (index < stockState.lastIndex) HorizontalDivider(color = SGray100, modifier = Modifier.padding(start = 70.dp))
            }

            item {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(14.dp)).border(1.5.dp, SGray200, RoundedCornerShape(14.dp))
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { showAddSheet = true }.padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Add, null, tint = SBlack, modifier = Modifier.size(16.dp))
                        Text("Añadir producto al inventario", fontSize = 14.sp, fontWeight = FontWeight.W600, color = SBlack)
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        NewProductSheet(onDismiss = { showAddSheet = false }, onSave = { showAddSheet = false })
    }
}

@Composable
private fun StockProductRow(product: ProductItem, onAdd: () -> Unit, onRemove: () -> Unit) {
    val stockColor = when { product.stock == 0 -> SRed; product.stock <= 3 -> SAmber; else -> SAccent }
    val maxVisualStock = 15

    Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 14.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(11.dp)).background(product.color.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                    Text(product.name.take(2).uppercase(), fontSize = 11.sp, fontWeight = FontWeight.W700, color = product.color)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.W600, color = SBlack, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                        AnimatedContent(
                            targetState = product.stock,
                            transitionSpec = {
                                if (targetState > initialState) slideInVertically(tween(180)) { -it } + fadeIn() togetherWith slideOutVertically(tween(140)) { it } + fadeOut()
                                else slideInVertically(tween(180)) { it } + fadeIn() togetherWith slideOutVertically(tween(140)) { -it } + fadeOut()
                            }, label = "stock_num_${product.id}"
                        ) { qty -> Text("$qty uds", fontSize = 13.sp, fontWeight = FontWeight.W700, color = stockColor) }
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)).background(
                        SGray100
                    )) {
                        Box(modifier = Modifier.fillMaxWidth((product.stock.toFloat() / maxVisualStock).coerceIn(0f, 1f)).fillMaxHeight().clip(RoundedCornerShape(2.dp)).background(stockColor))
                    }
                    if (product.stock == 0) Text("Sin stock", fontSize = 10.sp, fontWeight = FontWeight.W700, color = SRed)
                    else if (product.stock <= 3) Text("Stock bajo · reponer pronto", fontSize = 10.sp, color = SAmber, fontWeight = FontWeight.W600)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StockControlBtn(Icons.Default.Remove, enabled = product.stock > 0, onClick = onRemove)
                    StockControlBtn(Icons.Default.Add,    enabled = true, filled = true, onClick = onAdd)
                }
            }
        }
    }
}

@Composable
private fun StockControlBtn(icon: androidx.compose.ui.graphics.vector.ImageVector, enabled: Boolean, filled: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(30.dp).clip(CircleShape)
            .background(when { !enabled -> SGray100; filled -> SBlack; else -> Color.Transparent })
            .border(width = if (!filled && enabled) 1.dp else 0.dp, color = SGray200, shape = CircleShape)
            .clickable(enabled = enabled, interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = when { !enabled -> SGray400; filled -> Color.White; else -> SBlack }, modifier = Modifier.size(13.dp))
    }
}

@Composable
private fun StockAlertChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color) {
    Row(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = 0.12f)).padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(12.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.W600, color = color)
    }
}

@Composable
private fun MiniStockCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(12.dp)).background(Color.White).padding(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.W800, color = color)
            Text(label, fontSize = 11.sp, color = SGray400)
        }
    }
}