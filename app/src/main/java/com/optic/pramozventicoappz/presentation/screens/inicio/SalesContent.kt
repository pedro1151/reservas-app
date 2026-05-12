// SalesContent.kt
package com.optic.pramozventicoappz.presentation.sales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.SaleResponse
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultEmptyState
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.inicio.components.SaleCard
import com.optic.pramozventicoappz.presentation.screens.inicio.header.SaleFullHeader
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel
import java.time.LocalDate

@Composable
fun SalesContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
    sales: List<SaleResponse>,
    onMenuClick: () -> Unit,
    newSaleViewModel: NewSaleViewModel
) {

    val Background = Color(0xFFF8F4F6)
    val TextSoft = Color(0xFF6B7280)

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

    val grouped = sales
        .sortedByDescending { it.created }
        .groupBy { parseDate(it.created) }

    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            contentPadding = PaddingValues(
                bottom = paddingValues.calculateBottomPadding() + 100.dp
            ),
            state = listState
        )  {

            item {
                SaleFullHeader(
                    todayTotal = todayTotal,
                    todayCount = todayCount,
                    yesterdayTotal = yesterdayTotal,
                    monthTotal = monthTotal,
                    balanceHidden = balanceHidden,
                    onToggleHide = { balanceHidden = !balanceHidden },
                    navController = navController,
                    listState = listState,
                    onMenuClick = onMenuClick,
                    newSaleViewModel = newSaleViewModel
                )
            }

            grouped.forEach { (date, daySales) ->

                item {

                    val label = when (date) {
                        today -> "Hoy"
                        today.minusDays(1) -> "Ayer"
                        else -> date.toString()
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            color = Color(0xFF111827)
                        )

                        Text(
                            text = "${daySales.size} ventas",
                            color = TextSoft
                        )
                    }
                }

                items(daySales) { sale ->
                    SaleCard(
                        sale = sale,
                        navController = navController
                    )
                }
            }

            if (sales.isEmpty()) {
                item {

                    Spacer(Modifier.height(15.dp))
                    DefaultEmptyState(
                        icon = Icons.Default.Bolt,
                        title = "Sin ventas aún",
                        message = "Registra tu primer venta.",
                        buttonText = "Crear venta",
                        onAddClick = {
                            navController.navigate(
                                ClientScreen.CompleteSaleStepTwo.route
                            )
                        }
                    )
                }
            }
        }
    }
}