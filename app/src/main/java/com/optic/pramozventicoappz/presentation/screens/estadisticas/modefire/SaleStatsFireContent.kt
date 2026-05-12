package com.optic.pramozventicoappz.presentation.screens.estadisticas.modefire

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.SalesStatsResponse
import com.optic.pramozventicoappz.presentation.screens.estadisticas.SaleStatsViewModel
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.ModeSelector
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.YearSelector
import com.optic.pramozventicoappz.presentation.ui.theme.GradientBackground
import com.optic.pramozventicoappz.presentation.ui.theme.GrisModerno
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import kotlin.math.roundToInt

@Composable
fun SalesStatsFireContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    stats: SalesStatsResponse,
    viewModel: SaleStatsViewModel
) {
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMode by viewModel.selectedMode.collectAsState()

    var selectedPeriod by remember { mutableStateOf(1) }
    val periods = listOf("Hoy", "Semana", "Mes", "Año")

    val chartData = remember(stats, selectedPeriod) {
        mapStatsToChartDataGamificada(stats, selectedPeriod)
    }

    val monthlyGrowthMap = remember(stats, selectedPeriod) {
        if (selectedPeriod == 3) {
            stats.months.associate { it.name.take(3) to (it.growthPercentage ?: 0.0) }
        } else {
            emptyMap()
        }
    }

    val totalRevenue = chartData.sumOf { it.second }
    val topProducts = stats.productosTop.take(20)
    val topSalesmen = stats.salesmanRank.take(10)
    val paymentMethods = stats.paymentMethods

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GradientBackground)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("🔥 Tus estadísticas", color = Color.White)

                            Text(
                                "$ ${totalRevenue.roundToInt()}",
                                color = Color.White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            Row {
                                periods.forEachIndexed { index, label ->
                                    Text(
                                        label,
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .clickable { selectedPeriod = index },
                                        color = if (selectedPeriod == index) {
                                            Color.White
                                        } else {
                                            Color.White.copy(0.6f)
                                        }
                                    )
                                }
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            YearSelector(
                                selectedYear = selectedYear,
                                onYearSelected = { year ->
                                    viewModel.setYear(year)
                                }
                            )

                            ModeSelector(
                                selectedMode = selectedMode,
                                onModeSelected = { mode ->
                                    viewModel.setMode(mode)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(26.dp))
            }

            item {
                FireSectionLabel(text = "Rendimiento")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            item {
                FireCleanCard(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {


                    BarChartGamificado(
                        data = chartData,
                        growthByLabel = monthlyGrowthMap,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp)
                    )
                }
            }

            item {
                Spacer(Modifier.height(34.dp))
            }

            item {
                FireSectionLabel(text = "Resumen")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            stats.currentMonth?.let { month ->
                item {
                    FireCurrentMonthCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        name = month.name,
                        total = month.total,
                        cantidadVentas = month.cantidadVentas,
                        growthPercentage = month.growthPercentage
                    )
                }

                item {
                    Spacer(Modifier.height(14.dp))
                }
            }

            item {
                FireTodayComparisonCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    today = stats.today,
                    yesterday = stats.yesterday,
                    percentage = stats.todayVsYesterdayPercentage
                )
            }

            item {
                Spacer(Modifier.height(14.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FireMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Hoy",
                        value = "$ ${(stats.today ?: 0.0).roundToInt()}",
                        icon = Icons.Outlined.Bolt
                    )

                    FireMetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Ayer",
                        value = "$ ${(stats.yesterday ?: 0.0).roundToInt()}",
                        icon = Icons.Outlined.Today
                    )
                }
            }

            item {
                Spacer(Modifier.height(36.dp))
            }

            item {
                FireSectionLabel(text = "Crecimiento")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "En el año",
                            value = "$ ${stats.yearTotal.roundToInt()}",
                            valueExtra = "${stats.cantidadVentasYear} Ventas",
                            icon = Icons.Outlined.Schedule
                        )

                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Prom. diario",
                            value = "$ ${stats.promedioDiarioYear.roundToInt()}",
                            icon = Icons.Outlined.TrendingUp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Ticket prom.",
                            value = "$ ${stats.ticketPromedioYear.roundToInt()}",
                            icon = Icons.Outlined.Payments
                        )

                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Items año",
                            value = stats.cantidadItemsYear.toString(),
                            icon = Icons.Outlined.Inventory2
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(36.dp))
            }

            item {
                FireSectionLabel(text = "Negocio")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Ventas año",
                            value = stats.cantidadVentasYear.toString(),
                            icon = Icons.Outlined.ReceiptLong
                        )

                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Ventas histórico",
                            value = stats.cantidadVentasHistorico.toString(),
                            icon = Icons.Outlined.Bolt
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Productos",
                            value = stats.cantidadProductosTotal.toString(),
                            icon = Icons.Outlined.Category
                        )

                        FireMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Clientes",
                            value = stats.cantidadClientesTotal.toString(),
                            icon = Icons.Outlined.Person
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(36.dp))
            }

            item {
                FireSectionLabel(text = "Insights")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            stats.mainPaymentMethod?.let { method ->
                item {
                    FireMainPaymentMethodCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        method = method
                    )
                }

                item {
                    Spacer(Modifier.height(14.dp))
                }
            }

            stats.bestProduct?.let { product ->
                item {
                    FireBestProductHighlightCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        product = product
                    )
                }

                item {
                    Spacer(Modifier.height(14.dp))
                }
            }

            stats.bestMonth?.let { bestMonth ->
                item {
                    FireBestMonthCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        name = bestMonth.name,
                        total = bestMonth.total
                    )
                }
            }

            item {
                Spacer(Modifier.height(36.dp))
            }

            item {
                FireSectionLabel(text = "Rankings")
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            if (paymentMethods.isNotEmpty()) {
                item {
                    FirePaymentMethodsCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        paymentMethods = paymentMethods
                    )
                }

                item {
                    Spacer(Modifier.height(18.dp))
                }
            }

            if (topProducts.isNotEmpty()) {
                item {
                    FireTopProductsCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        topProducts = topProducts
                    )
                }

                item {
                    Spacer(Modifier.height(18.dp))
                }
            }

            if (topSalesmen.isNotEmpty()) {
                item {
                    FireSalesmanRankCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        salesmen = topSalesmen
                    )
                }
            }

        }
    }
}

@Composable
private fun FireSectionLabel(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 18.dp),
        color = TextPrimary.copy(alpha = 0.90f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.1.sp
    )
}