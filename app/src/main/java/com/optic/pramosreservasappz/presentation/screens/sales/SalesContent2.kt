package com.optic.pramosreservasappz.presentation.sales.Components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.Components.RegisterSaleSheet
import com.optic.pramosreservasappz.presentation.screens.sales.Components.SaleCard
import java.time.LocalDate

@Composable
fun SalesContent2(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    viewModel: SalesViewModel,
    navController: NavHostController,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    sales: List<SaleResponse> // 🔥 NUEVO
) {
    var showRegisterSale by remember { mutableStateOf(false) }
    var balanceHidden by remember { mutableStateOf(false) }


    val today = LocalDate.now()

    fun parseDate(date: String): LocalDate {
        return LocalDate.parse(date.substring(0, 10))
    }

    val todaySales = sales.filter { parseDate(it.created) == today }
    val todayTotal = todaySales.sumOf { it.amount }
    val todayCount = todaySales.size

    val yesterdayTotal = sales
        .filter { parseDate(it.created) == today.minusDays(1) }
        .sumOf { it.amount }

    val monthTotal = sales
        .filter { parseDate(it.created).month == today.month }
        .sumOf { it.amount }

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        StatsHeader(
            todayTotal = todayTotal,
            todayCount = todayCount,
            yesterdayTotal = yesterdayTotal,
            monthTotal = monthTotal,
            balanceHidden = balanceHidden,
            onToggleHide = { balanceHidden = !balanceHidden },
            onRegisterSale = { showRegisterSale = true }
        )

        SalesHomeTab(
            sales = sales,
            today = today
        )
    }

    // 🔥 REGISTRAR VENTA (SIMPLIFICADO)
    if (showRegisterSale) {
        RegisterSaleSheet(
            onDismiss = { showRegisterSale = false },
            viewModel = viewModel
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
            .background(Color.White)
            .padding(horizontal = 5.dp, vertical = 28.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier              = Modifier.fillMaxWidth()
            ) {
                Text("HOY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W600,
                    color = Violet,
                    letterSpacing = 1.5.sp
                )
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
                    text = if (hidden) "$ ••••" else "$ ${"%.2f".format(total)}",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.W700,
                    color = SBlack,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(" $todayCount ${if (todayCount == 1) "ventas" else "ventas"}",
                fontSize = 13.sp,
                color = SGray600
            )
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                StatPill(label = "Ayer",     value = if (balanceHidden) "••••" else "$ ${"%.0f".format(yesterdayTotal)}")
                StatPill(label = "Este mes", value = if (balanceHidden) "••••" else "$ ${"%.0f".format(monthTotal)}")
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Violet)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, onClick = onRegisterSale
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp))
                {
                    Icon(Icons.Default.Bolt,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                    Text("Venta rapida",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W700,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label,
            fontSize = 14.sp,
            color = Color(0xFF777777)
        )
        Spacer(Modifier.height(2.dp))
        Text(value,
            fontSize = 15.sp,
            color = SBlack,
            fontWeight = FontWeight.W600
        )
    }
}



@Composable
private fun SalesHomeTab(
    sales: List<SaleResponse>,
    today: LocalDate
) {

    fun parseDate(date: String): LocalDate {
        return LocalDate.parse(date.substring(0, 10))
    }

    val grouped = sales
        .sortedByDescending { it.created }
        .groupBy { parseDate(it.created) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {

        grouped.forEach { (date, daySales) ->

            item {
                val label = when (date) {
                    today -> "Hoy"
                    today.minusDays(1) -> "Ayer"
                    else -> date.toString()
                }

                val total = daySales.sumOf { it.amount }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(label)
                    Text("$ ${"%.0f".format(total)}")
                }
            }

            items(daySales) { sale ->
                SaleCard(sale)
            }
        }

        if (sales.isEmpty()) {
            item {
                Box(
                    Modifier.fillMaxWidth().padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin ventas aún")
                }
            }
        }
    }
}


