package com.optic.pramosreservasappz.presentation.sales.Components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.screens.sales.Components.SaleCard
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.Components.SaleResumeList
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleFullHeader
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleHeader
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleMiniHeader
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import java.time.LocalDate

@Composable
fun SalesContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
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

    val listState = rememberLazyListState()


    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(SoftCoolBackground)
    ) {


        fun parseDate(date: String): LocalDate {
            return LocalDate.parse(date.substring(0, 10))
        }

        val grouped = sales
            .sortedByDescending { it.created }
            .groupBy { parseDate(it.created) }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(SoftCoolBackground),
            contentPadding = PaddingValues(bottom = 100.dp),
            state = listState
        ) {
            item{
                SaleFullHeader(
                    todayTotal = todayTotal,
                    todayCount = todayCount,
                    yesterdayTotal = yesterdayTotal,
                    monthTotal = monthTotal,
                    balanceHidden = balanceHidden,
                    onToggleHide = { balanceHidden = !balanceHidden },
                    navController = navController,
                    listState = listState
                )
            }

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
                        //Text("$ ${"%.0f".format(total)}")
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


}

