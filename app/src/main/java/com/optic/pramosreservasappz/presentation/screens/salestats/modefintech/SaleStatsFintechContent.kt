package com.optic.pramosreservasappz.presentation.screens.salestats.modefintech

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.*
import com.optic.pramosreservasappz.presentation.screens.salestats.SaleStatsViewModel
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Cyan
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Purple
import com.optic.pramosreservasappz.presentation.screens.salestats.components.ModeSelector
import com.optic.pramosreservasappz.presentation.screens.salestats.components.YearSelector
import kotlin.math.roundToInt
import kotlin.math.roundToLong

// 🔥 ---------- MAPPER ----------
fun mapStatsToChartData(
    stats: SalesStatsResponse,
    selectedPeriod: Int
): List<Pair<String, Double>> {

    return when (selectedPeriod) {

        0 -> listOf("Hoy" to (stats.today ?: 0.0))

        1 -> stats.lastWeek?.let {
            listOf(
                "L" to it.lunes,
                "M" to it.martes,
                "X" to it.miercoles,
                "J" to it.jueves,
                "V" to it.viernes,
                "S" to it.sabado,
                "D" to it.domingo
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

// 🔥 ---------- LINE CHART PRO (FINTECH) ----------
@Composable
fun SmoothLineChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {

    val max = (data.maxOfOrNull { it.second } ?: 1.0).toFloat()

    // 🔥 ANIMACIÓN REAL (reemplaza animateFloatAsState)
    val pathProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        pathProgress.snapTo(0f)
        pathProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(modifier = modifier) {

        // 🔥 CHART
        Box(modifier = Modifier.weight(1f)) {

            Canvas(modifier = Modifier.fillMaxSize()) {

                val stepX = size.width / (data.size - 1).coerceAtLeast(1)

                val points = data.mapIndexed { i, entry ->
                    val x = i * stepX
                    val y = size.height - (entry.second.toFloat() / max) * size.height
                    Offset(x, y)
                }

                val path = Path()

                points.forEachIndexed { i, point ->
                    if (i == 0) path.moveTo(point.x, point.y)
                    else path.lineTo(point.x, point.y)
                }

                val measure = PathMeasure()
                measure.setPath(path, false)

                val segment = Path()
                measure.getSegment(0f, measure.length * pathProgress.value, segment)

                // 🔥 ÁREA BAJO CURVA (FINTECH PRO)
                val fillPath = Path().apply {
                    addPath(segment)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF7C3AED).copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )

                // 🔥 GLOW SUAVE
                drawPath(
                    path = segment,
                    color = Color(0xFF7C3AED).copy(alpha = 0.2f),
                    style = Stroke(width = 8.dp.toPx())
                )

                // 🔥 LÍNEA PRINCIPAL CON GRADIENTE
                drawPath(
                    path = segment,
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF7C3AED),
                            Color(0xFFA78BFA)
                        )
                    ),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                // 🔥 PUNTOS CON APARICIÓN PROGRESIVA
                points.forEachIndexed { index, point ->

                    val pointProgress =
                        (pathProgress.value * points.size - index)
                            .coerceIn(0f, 1f)

                    if (pointProgress > 0f) {

                        drawCircle(
                            color = Color.White.copy(alpha = pointProgress),
                            radius = (5.dp * pointProgress).toPx(),
                            center = point
                        )

                        drawCircle(
                            color = Color(0xFF7C3AED).copy(alpha = pointProgress),
                            radius = (3.dp * pointProgress).toPx(),
                            center = point
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🔥 LABELS + MONTOS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { (label, value) ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "$ ${"%.0f".format(value)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFF7C3AED)
                    )
                }
            }
        }
    }
}

// 🔥 ---------- GLASS CARD ----------
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.4f),
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Column(content = content)
    }
}

// 🔥 ---------- SCREEN ----------
@Composable
fun SalesStatsFintechContent(
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

    val totalRevenue = when (selectedPeriod) {
        0 -> stats.today ?: 0.0
        else -> chartData.sumOf { it.second }
    }

    // 🔥 crecimiento semanal
    val weekValues = stats.lastWeek?.let {
        listOf(it.lunes, it.martes, it.miercoles, it.jueves, it.viernes, it.sabado, it.domingo)
    } ?: emptyList()

    val currentWeek = weekValues.sum()
    val previousWeek = weekValues.take(3).sum() // simple aproximación

    val growth = if (previousWeek > 0)
        ((currentWeek - previousWeek) / previousWeek) * 100 else 0.0

    val topProducts = stats.productosTop.take(20)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
    ) {

        LazyColumn(
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            // 🔥 HEADER PRO
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Purple, Cyan)
                            )
                        )
                        .padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column {

                            Text(
                                "Tus estadisticas",
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            Text(
                                "Bs ${"%.0f".format(totalRevenue)}",
                                color = Color.White,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "${growth.roundToInt()}% vs semana anterior",
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Spacer(Modifier.height(16.dp))

                            Row {
                                periods.forEachIndexed { index, label ->
                                    Text(
                                        label,
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .clickable { selectedPeriod = index },
                                        color = if (selectedPeriod == index) Color.White else Color.White.copy(
                                            alpha = 0.6f
                                        )
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

            // 🔥 CHART
            item {
                GlassCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Ingresos", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    SmoothLineChart(
                        data = chartData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                }
            }

            // 🔥 CARDS
            item {
                Row(
                    Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    GlassCard(modifier = Modifier.weight(1f)) {
                        Text("Hoy", fontSize = 12.sp)
                        Text("$ ${"%.0f".format(stats.today ?: 0.0)}", fontWeight = FontWeight.Bold)
                    }

                    GlassCard(modifier = Modifier.weight(1f)) {
                        Text("Año", fontSize = 12.sp)
                        Text("$ ${"%.0f".format(stats.yearTotal)}", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 🔥 TOP PRODUCTOS
            item {
                GlassCard(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {

                    Text("Top productos", fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(12.dp))

                    val max = topProducts.maxOfOrNull { it.totalVendido } ?: 1.0

                    topProducts.forEach { product ->

                        val progress = (product.totalVendido / max).toFloat()

                        // 🔥 ANIMACIÓN REAL (como FIRE)
                        val animatedProgress = remember(product.name) {
                            Animatable(0f)
                        }

                        LaunchedEffect(product.name, progress) {
                            animatedProgress.snapTo(0f)
                            animatedProgress.animateTo(
                                targetValue = progress,
                                animationSpec = tween(
                                    durationMillis = 900,
                                    easing = FastOutSlowInEasing // 🔥 más fintech
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(product.name, fontWeight = FontWeight.Medium)

                                Text(
                                    "$ ${product.totalVendido.roundToLong()}",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.height(6.dp))

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray.copy(0.3f))
                            ) {

                                // 🔥 BARRA ANIMADA IZQ → DER
                                Box(
                                    Modifier
                                        .fillMaxWidth(animatedProgress.value)
                                        .fillMaxHeight()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xFF7C3AED), // morado fuerte
                                                    Color(0xFFA78BFA)  // morado suave
                                                )
                                            )
                                        )
                                )
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}