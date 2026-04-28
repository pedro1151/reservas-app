package com.optic.pramosreservasappz.presentation.screens.inicio.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import java.time.LocalDate


@Composable
fun SaleResumeList(
    sales: List<SaleResponse>,
    today: LocalDate,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    navController: NavHostController
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


