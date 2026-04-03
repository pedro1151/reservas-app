package com.optic.pramosreservasappz.presentation.sales.Components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.Components.NewProductSheet
import com.optic.pramosreservasappz.presentation.screens.sales.Components.RecibosScreen
import com.optic.pramosreservasappz.presentation.screens.sales.Components.ReportesScreen
import com.optic.pramosreservasappz.presentation.screens.sales.Components.StockScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SalesContent(
    modifier:      Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: SalesViewModel,
    navController: NavHostController,
    selectedTab:   Int    = 0,
    onTabChange:   (Int) -> Unit = {}
) {
    var showRegisterSale by remember { mutableStateOf(false) }
    var showNewProduct   by remember { mutableStateOf(false) }
    var showReportes     by remember { mutableStateOf(false) }
    var showRecibos      by remember { mutableStateOf(false) }
    var showStock        by remember { mutableStateOf(false) }

    val salesList = remember { fakeSales.toMutableStateList() }
    var balanceHidden by remember { mutableStateOf(false) }

    val today          = LocalDate.now()
    val todaySales     = salesList.filter { it.date == today && it.status == SaleStatus.COMPLETED }
    val todayTotal     = todaySales.sumOf { it.total }
    val todayCount     = todaySales.size
    val yesterdayTotal = salesList.filter { it.date == today.minusDays(1) && it.status == SaleStatus.COMPLETED }.sumOf { it.total }
    val monthTotal     = salesList.filter { it.date.month == today.month && it.status == SaleStatus.COMPLETED }.sumOf { it.total }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            StatsHeader(
                todayTotal      = todayTotal,
                todayCount      = todayCount,
                yesterdayTotal  = yesterdayTotal,
                monthTotal      = monthTotal,
                balanceHidden   = balanceHidden,
                onToggleHide    = { balanceHidden = !balanceHidden },
                onRegisterSale  = { showRegisterSale = true }
            )

            SalesTabs(selectedTab = selectedTab, onTabChange = onTabChange)

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    val dir = if (targetState > initialState) 1 else -1
                    (slideInHorizontally(tween(260)) { it * dir } + fadeIn(tween(220))) togetherWith
                            (slideOutHorizontally(tween(240)) { -it * dir } + fadeOut(tween(180)))
                },
                label = "sales_tab"
            ) { tab ->
                when (tab) {
                    0 -> SalesHomeTab(
                        sales      = salesList,
                        today      = today,
                        onReportes = { showReportes = true },
                        onRecibos  = { showRecibos = true },
                        onStock    = { showStock = true }
                    )
                    else -> ProductsTab(
                        products       = fakeProducts,
                        onNewProduct   = { showNewProduct = true }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible      = showReportes,
            enter        = slideInVertically(tween(350, easing = EaseOutCubic)) { it } + fadeIn(tween(250)),
            exit         = slideOutVertically(tween(300, easing = EaseInCubic)) { it } + fadeOut(tween(200)),
            modifier     = Modifier.fillMaxSize()
        ) {
            ReportesScreen(onBack = { showReportes = false }, modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible      = showRecibos,
            enter        = slideInVertically(tween(350, easing = EaseOutCubic)) { it } + fadeIn(tween(250)),
            exit         = slideOutVertically(tween(300, easing = EaseInCubic)) { it } + fadeOut(tween(200)),
            modifier     = Modifier.fillMaxSize()
        ) {
            RecibosScreen(onBack = { showRecibos = false }, modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible      = showStock,
            enter        = slideInVertically(tween(350, easing = EaseOutCubic)) { it } + fadeIn(tween(250)),
            exit         = slideOutVertically(tween(300, easing = EaseInCubic)) { it } + fadeOut(tween(200)),
            modifier     = Modifier.fillMaxSize()
        ) {
            StockScreen(onBack = { showStock = false }, modifier = Modifier.fillMaxSize())
        }
    }
   /*
    if (showRegisterSale) {
        RegistrarVentaFicticio(
            onDismiss = { showRegisterSale = false },
            onConfirm = { newSale ->
                salesList.add(0, newSale)
                fakeSales.add(0, newSale)
            }
        )
    }

    */

    if (showNewProduct) {
        NewProductSheet(
            onDismiss = { showNewProduct = false },
            onSave    = { newProduct ->
                fakeProducts.add(newProduct)
                showNewProduct = false
            }
        )
    }
}

@Composable
private fun StatsHeader(
    todayTotal:     Double,
    todayCount:     Int,
    yesterdayTotal: Double,
    monthTotal:     Double,
    balanceHidden:  Boolean,
    onToggleHide:   () -> Unit,
    onRegisterSale: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xFF111111), Color(0xFF1C1C1C))))
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier              = Modifier.fillMaxWidth()
            ) {
                Text("HOY", fontSize = 11.sp, fontWeight = FontWeight.W600, color = SAccent, letterSpacing = 1.5.sp)
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onToggleHide),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (balanceHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        null, tint = Color(0xFF888888), modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedContent(
                targetState = balanceHidden to todayTotal,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "balance"
            ) { (hidden, total) ->
                Text(
                    text = if (hidden) "Bs ••••" else "Bs ${"%.2f".format(total)}",
                    fontSize = 38.sp, fontWeight = FontWeight.W700, color = Color.White, letterSpacing = (-1).sp
                )
            }

            Spacer(Modifier.height(4.dp))
            Text("en $todayCount ${if (todayCount == 1) "venta completada" else "ventas completadas"}", fontSize = 13.sp, color = SAccent)
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                StatPill(label = "Ayer",     value = if (balanceHidden) "••••" else "Bs ${"%.0f".format(yesterdayTotal)}")
                StatPill(label = "Este mes", value = if (balanceHidden) "••••" else "Bs ${"%.0f".format(monthTotal)}")
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SAccent)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onRegisterSale)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Registrar venta", fontSize = 15.sp, fontWeight = FontWeight.W700, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = Color(0xFF777777))
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.W600)
    }
}

@Composable
private fun SalesTabs(selectedTab: Int, onTabChange: (Int) -> Unit) {
    val tabs = listOf("Ventas", "Productos")
    Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onTabChange(index) }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        title,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == index) FontWeight.W700 else FontWeight.W400,
                        color = if (selectedTab == index) SBlack else SGray400,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    AnimatedVisibility(visible = selectedTab == index, enter = fadeIn() + expandHorizontally(), exit = fadeOut() + shrinkHorizontally()) {
                        Box(Modifier.width(24.dp).height(2.dp).clip(CircleShape).background(SBlack))
                    }
                    if (selectedTab != index) Spacer(Modifier.height(2.dp))
                }
            }
        }
    }
    HorizontalDivider(color = SGray200, thickness = 0.5.dp)
}

@Composable
private fun SalesHomeTab(
    sales:      List<SaleItem>,
    today:      LocalDate,
    onReportes: () -> Unit,
    onRecibos:  () -> Unit,
    onStock:    () -> Unit
) {
    val grouped = remember(sales) {
        sales.sortedByDescending { it.date.toEpochDay() * 10000 + it.time.toSecondOfDay() }
            .groupBy { it.date }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(icon = Icons.Outlined.BarChart,   label = "Reportes", modifier = Modifier.weight(1f), onClick = onReportes)
                QuickActionCard(icon = Icons.Outlined.Receipt,    label = "Recibos",  modifier = Modifier.weight(1f), onClick = onRecibos)
                QuickActionCard(icon = Icons.Outlined.Inventory2, label = "Stock",    modifier = Modifier.weight(1f), onClick = onStock)
            }
        }

        grouped.forEach { (date, daySales) ->
            item(key = "header_${date}") {
                val label = when (date) {
                    today              -> "Hoy"
                    today.minusDays(1) -> "Ayer"
                    else               -> date.format(DateTimeFormatter.ofPattern("d MMM", java.util.Locale("es","ES")))
                }
                val dayTotal = daySales.filter { it.status == SaleStatus.COMPLETED }.sumOf { it.total }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, fontSize = 12.sp, fontWeight = FontWeight.W700, color = SGray600, letterSpacing = 0.5.sp)
                    Text("Bs ${"%.0f".format(dayTotal)}", fontSize = 12.sp, fontWeight = FontWeight.W600, color = SGray400)
                }
            }
            items(daySales, key = { "sale_${it.id}" }) { sale ->
                SaleRow(sale = sale)
                HorizontalDivider(color = SGray100, modifier = Modifier.padding(start = 72.dp))
            }
        }

        if (sales.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(top = 80.dp), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.PointOfSale, null, tint = SGray200, modifier = Modifier.size(44.dp))
                        Text("Sin ventas aún", fontSize = 14.sp, color = SGray400)
                        Text("Toca el botón + para registrar una venta", fontSize = 12.sp, color = SGray400, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(icon: ImageVector, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, SGray200, RoundedCornerShape(14.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, null, tint = SBlack, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.W500, color = SGray600)
        }
    }
}

@Composable
private fun SaleRow(sale: SaleItem) {
    val timeFmt = remember { java.time.format.DateTimeFormatter.ofPattern("HH:mm") }
    val avatarColor = remember(sale.id) {
        val palette = listOf(Color(0xFF1A1A1A), Color(0xFF4A6CF7), Color(0xFF7C3AED), Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706), Color(0xFF0891B2), Color(0xFFDB2777))
        palette[sale.id % palette.size]
    }
    val initials = remember(sale.clientName) {
        val parts = sale.clientName.trim().split(" ")
        if (parts.size == 1) parts[0].take(2).uppercase()
        else "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(42.dp).clip(CircleShape).background(if (sale.status == SaleStatus.CANCELLED) SGray200 else avatarColor),
            Alignment.Center
        ) {
            Text(initials, fontSize = 13.sp, fontWeight = FontWeight.W700, color = if (sale.status == SaleStatus.CANCELLED) SGray400 else Color.White)
        }

        Column(Modifier.weight(1f)) {
            Text(sale.clientName, fontSize = 14.sp, fontWeight = FontWeight.W500, color = if (sale.status == SaleStatus.CANCELLED) SGray400 else SBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(2.dp))
            Text(sale.items.joinToString(", "), fontSize = 12.sp, color = SGray400, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                if (sale.status == SaleStatus.CANCELLED) "-" else "Bs ${"%.0f".format(sale.total)}",
                fontSize = 14.sp, fontWeight = FontWeight.W700,
                color = when (sale.status) { SaleStatus.COMPLETED -> SBlack; SaleStatus.PENDING -> SAmber; SaleStatus.CANCELLED -> SGray400 }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(sale.time.format(timeFmt), fontSize = 10.sp, color = SGray400)
                if (sale.status != SaleStatus.COMPLETED) SaleStatusBadge(sale.status)
            }
        }
    }
}

@Composable
private fun SaleStatusBadge(status: SaleStatus) {
    val (color, label) = when (status) {
        SaleStatus.PENDING   -> SAmber to "Pendiente"
        SaleStatus.CANCELLED -> SRed to "Cancelado"
        else -> return
    }
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = 0.10f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.W700, color = color)
    }
}

@Composable
private fun ProductsTab(products: List<ProductItem>, onNewProduct: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).border(1.dp, SGray200, RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Column {
                        Text(products.size.toString(), fontSize = 28.sp, fontWeight = FontWeight.W800, color = SBlack)
                        Text("PRODUCTOS\nREGISTRADOS", fontSize = 10.sp, fontWeight = FontWeight.W600, color = SGray400, letterSpacing = 0.5.sp)
                    }
                }
                Box(
                    modifier = Modifier.weight(1.4f).clip(RoundedCornerShape(16.dp)).background(SBlack)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onNewProduct).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        Text("Nuevo producto", fontSize = 13.sp, fontWeight = FontWeight.W600, color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(12.dp)).background(Color.White).border(1.dp, SGray200, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Search, null, tint = SGray400, modifier = Modifier.size(18.dp))
                Text("Ítem, valor o código", fontSize = 14.sp, color = SGray400)
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier.size(26.dp).clip(CircleShape).background(SAccent)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onNewProduct),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }

        items(products, key = { "prod_${it.id}" }) { product ->
            ProductRow(product = product)
            HorizontalDivider(color = SGray100, modifier = Modifier.padding(start = 72.dp))
        }
    }
}

@Composable
private fun ProductRow(product: ProductItem) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(11.dp)).background(product.color.copy(alpha = 0.13f)), contentAlignment = Alignment.Center) {
            Box(Modifier.width(3.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 11.dp, bottomStart = 11.dp)).background(product.color).align(Alignment.CenterStart))
            Text(product.name.take(2).uppercase(), fontSize = 11.sp, fontWeight = FontWeight.W700, color = product.color)
        }
        Text(product.name, modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.W500, color = SBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Column(horizontalAlignment = Alignment.End) {
            Text("Bs ${"%.2f".format(product.price)}", fontSize = 14.sp, fontWeight = FontWeight.W700, color = SBlack)
            if (product.stock > 0) Text("${product.stock} en stock", fontSize = 10.sp, color = SGray400)
        }
        Icon(Icons.Outlined.ChevronRight, null, tint = SGray200, modifier = Modifier.size(18.dp))
    }
}