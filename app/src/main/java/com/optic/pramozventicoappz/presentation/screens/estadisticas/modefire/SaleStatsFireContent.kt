package com.optic.pramozventicoappz.presentation.screens.estadisticas.modefire

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.*
import com.optic.pramozventicoappz.presentation.screens.estadisticas.SaleStatsViewModel
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Cyan
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.LightGrayBg
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Purple
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.chartColors
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.ModeSelector
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.YearSelector
import com.optic.pramozventicoappz.presentation.ui.theme.GradientBackground
import com.optic.pramozventicoappz.presentation.ui.theme.GrisModerno
import kotlin.math.roundToInt
import kotlin.math.roundToLong

// 🔥 ---------- MAPPER ----------
private fun mapStatsToChartDataGamificada(
    stats: SalesStatsResponse,
    selectedPeriod: Int
): List<Pair<String, Double>> {
    return when (selectedPeriod) {

        0 -> listOf("Hoy" to (stats.today ?: 0.0))

        1 -> stats.lastWeek?.let {
            listOf(
                "LU" to it.lunes,
                "MA" to it.martes,
                "MI" to it.miercoles,
                "JUE" to it.jueves,
                "VIE" to it.viernes,
                "SAB" to it.sabado,
                "DOM" to it.domingo
            )
        } ?: emptyList()

        2 -> {
            val month = stats.months.maxByOrNull { it.monthIndex }
            month?.weeks?.mapIndexed { i, w ->
                "S${i + 1}" to w.monto
            } ?: emptyList()
        }

        else -> stats.months.map {
            it.name.take(3) to it.total
        }
    }
}


// 🔥 ---------- BARCHART ----------
@Composable
private fun BarChartGamificado(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {

    val maxRaw = data.maxOfOrNull { it.second } ?: 0.0
    val max = if (maxRaw <= 0.0) 1.0 else maxRaw

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {

        // 🔥 usar forEachIndexed (FIX)
        data.forEachIndexed { index, (label, value) ->

            val color = chartColors[index % chartColors.size]

            val ratio = (value / max).toFloat()

            val animatedHeight = remember(label) {
                Animatable(0f)
            }

            LaunchedEffect(label, ratio) {
                animatedHeight.snapTo(0f)
                animatedHeight.animateTo(
                    targetValue = ratio,
                    animationSpec = tween(800)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    "$ ${value.roundToInt()}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .width(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray.copy(0.2f)),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    // 🔥 barra con color dinámico
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(animatedHeight.value)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        color.copy(alpha = 0.9f),
                                        color.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    label,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


// 🔥 ---------- CARD FINTECH ----------
@Composable
fun CleanCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp) // 🔥 sombra sutil
    ) {
        Column(
            modifier = Modifier.padding(18.dp) // 🔥 más aire interno
        ) {
            content()
        }
    }
}



// 🔥 ---------- SCREEN ----------
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

    val totalRevenue = chartData.sumOf { it.second }

    val topProducts = stats.productosTop.take(20)



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {

        LazyColumn {

            // 🔥 HEADER
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

                        // 🔥 LEFT CONTENT
                        Column {

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
                                        color = if (selectedPeriod == index)
                                            Color.White else Color.White.copy(0.6f)
                                    )
                                }
                            }
                        }

                        // 🔥 RIGHT CONTENT (COMBOS)
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            YearSelector(
                                selectedYear = selectedYear,
                                onYearSelected = { year ->
                                    viewModel.setYear(year) // 🔥 dispara carga
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

            // 🔥 CHART BARRAS
            item {
                CleanCard(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 1.dp)
                ) {
                    Text("📊 Rendimiento",
                        fontWeight = FontWeight.Bold,
                        color = GrisModerno
                    )
                    Spacer(Modifier.height(16.dp))

                    BarChartGamificado(
                        data = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }

            // 🔥 CARDS HOY / AÑO
            item {
                Row(
                    Modifier
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CleanCard(modifier = Modifier.weight(1f)) {
                        Text("Hoy",
                            fontSize = 12.sp,
                            color = GrisModerno
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$ ${(stats.today ?: 0.0).roundToInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    CleanCard(modifier = Modifier.weight(1f)) {
                        Text("En el Año",
                            fontSize = 12.sp,
                            color = GrisModerno
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$ ${stats.yearTotal.roundToInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // 🔥 TOP PRODUCTOS
            item {
                CleanCard(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Text("🏆 Top productos", fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(14.dp))

                    val max = topProducts.maxOfOrNull { it.totalVendido } ?: 1.0

                    topProducts.forEach { product ->

                        val progress = (product.totalVendido / max).toFloat()

                        val animatedProgress = remember(product.name) {
                            Animatable(0f)
                        }

                        LaunchedEffect(product.name, progress) {
                            animatedProgress.snapTo(0f)
                            animatedProgress.animateTo(
                                targetValue = progress,
                                animationSpec = tween(900)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(product.name)

                                Text(
                                    "$ ${product.totalVendido.roundToLong()}",
                                    fontWeight = FontWeight.Bold,
                                    color = GrisModerno
                                )
                            }

                            Spacer(Modifier.height(6.dp))

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.LightGray.copy(0.25f))
                            ) {

                                // 🔥 BARRA ANIMADA
                                Box(
                                    Modifier
                                        .fillMaxWidth(animatedProgress.value)
                                        .fillMaxHeight()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Cyan.copy(alpha = 0.9f),
                                                    Purple.copy(alpha = 0.7f)
                                                )
                                            )
                                        )
                                )

                                // 🔥 TEXTO AL FINAL DE LA BARRA (overlay fintech)
                                Text(
                                    text = "$ ${product.totalVendido.roundToInt()}",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}