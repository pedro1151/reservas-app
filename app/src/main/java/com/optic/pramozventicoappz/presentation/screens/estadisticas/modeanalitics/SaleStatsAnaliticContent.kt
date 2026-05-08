package com.optic.pramozventicoappz.presentation.screens.estadisticas.modeanalitics


import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.*
import com.optic.pramozventicoappz.presentation.screens.estadisticas.SaleStatsViewModel
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Cyan
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Purple
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.ModeSelector
import com.optic.pramozventicoappz.presentation.screens.estadisticas.components.YearSelector
import com.optic.pramozventicoappz.presentation.screens.estadisticas.modefintech.mapStatsToChartData
import com.optic.pramozventicoappz.presentation.ui.theme.GradientBackground
import kotlin.math.roundToInt
import kotlin.math.roundToLong

// 🎨 PALETA ANALYTICS
val AnalyticsBlue = Color(0xFF2563EB)
val AnalyticsGreen = Color(0xFF16A34A)
val AnalyticsOrange = Color(0xFFF59E0B)
val GridColor = Color(0xFFE5E7EB)

// ---------- CHART ANALYTICS (CON GRID) ----------
@Composable
fun AnalyticsLineChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {

    val max = (data.maxOfOrNull { it.second } ?: 1.0).toFloat()

    val progress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        progress.snapTo(0f)
        progress.animateTo(
            1f,
            animationSpec = tween(800, easing = LinearEasing) // ⚡ más preciso
        )
    }

    Column(modifier = modifier) {

        Box(modifier = Modifier.weight(1f)) {

            Canvas(modifier = Modifier.fillMaxSize()) {

                val stepX = size.width / (data.size - 1).coerceAtLeast(1)

                // 🔥 GRID HORIZONTAL
                val rows = 4
                repeat(rows) { i ->
                    val y = size.height * i / rows
                    drawLine(
                        color = GridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val points = data.mapIndexed { i, entry ->
                    val x = i * stepX
                    val y = size.height - (entry.second.toFloat() / max) * size.height
                    Offset(x, y)
                }

                val path = Path()
                points.forEachIndexed { i, p ->
                    if (i == 0) path.moveTo(p.x, p.y)
                    else path.lineTo(p.x, p.y)
                }

                val measure = PathMeasure()
                measure.setPath(path, false)

                val segment = Path()
                measure.getSegment(0f, measure.length * progress.value, segment)

                // 🔥 LÍNEA PRINCIPAL (más fina)
                drawPath(
                    path = segment,
                    color = AnalyticsBlue,
                    style = Stroke(width = 2.dp.toPx())
                )

                // 🔥 PUNTOS
                points.forEachIndexed { index, point ->

                    val pointProgress =
                        (progress.value * points.size - index)
                            .coerceIn(0f, 1f)

                    if (pointProgress > 0f) {
                        drawCircle(
                            color = AnalyticsBlue,
                            radius = (3.dp * pointProgress).toPx(),
                            center = point
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // LABELS
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { (label, value) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, fontSize = 10.sp, color = Color.Gray)
                    Text(
                        "${value.roundToInt()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ---------- CARD ANALYTICS ----------
@Composable
fun AnalyticsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, GridColor, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        content()
    }
}

// ---------- SCREEN ----------
@Composable
fun SalesStatsAnalyticsContent(
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
        mapStatsToChartData(stats, selectedPeriod)
    }

    val totalRevenue = chartData.sumOf { it.second }

    val topProducts = stats.productosTop.take(10)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn {

            // 🔥 HEADER (MISMO COLOR)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GradientBackground)
                        .padding(20.dp)
                ) {

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(
                                "Analytics",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace // 🔥 cambio clave
                            )

                            Text(
                                "Bs ${"%.0f".format(totalRevenue)}",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(12.dp))

                            Row {
                                periods.forEachIndexed { index, label ->
                                    Text(
                                        label,
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .clickable { selectedPeriod = index },
                                        color = if (selectedPeriod == index)
                                            Color.White else Color.White.copy(0.6f)
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {

                            YearSelector(
                                selectedYear = selectedYear,
                                onYearSelected = { viewModel.setYear(it) }
                            )

                            ModeSelector(
                                selectedMode = selectedMode,
                                onModeSelected = { viewModel.setMode(it) }
                            )
                        }
                    }
                }
            }

            // 📊 CHART
            item {
                AnalyticsCard(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Rendimiento", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))

                    AnalyticsLineChart(
                        data = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }

            // 📦 TOP PRODUCTS
            item {
                AnalyticsCard(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {

                    Text("Top Productos-Servicios", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))

                    val max = topProducts.maxOfOrNull { it.totalVendido } ?: 1.0

                    topProducts.forEach { product ->

                        val ratio = (product.totalVendido / max).toFloat()

                        val anim = remember(product.name) { Animatable(0f) }

                        LaunchedEffect(product.name, ratio) {
                            anim.snapTo(0f)
                            anim.animateTo(ratio, tween(700, easing = LinearEasing))
                        }

                        Column(Modifier.padding(vertical = 6.dp)) {

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(product.name, fontSize = 12.sp)
                                Text(
                                    "${product.totalVendido.roundToLong()}",
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(GridColor)
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth(anim.value)
                                        .fillMaxHeight()
                                        .background(AnalyticsGreen)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}