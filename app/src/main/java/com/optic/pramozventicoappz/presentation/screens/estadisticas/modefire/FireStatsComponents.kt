package com.optic.pramozventicoappz.presentation.screens.estadisticas.modefire

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingFlat
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.sales.PaymentMethodStats
import com.optic.pramozventicoappz.domain.model.sales.SalesStatsResponse
import com.optic.pramozventicoappz.domain.model.sales.SalesmanRankStats
import com.optic.pramozventicoappz.domain.model.sales.TopProductStats
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Cyan
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.Purple
import com.optic.pramozventicoappz.presentation.screens.estadisticas.colors.chartColors
import com.optic.pramozventicoappz.presentation.ui.theme.AccentText
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGrisSoftCard
import com.optic.pramozventicoappz.presentation.ui.theme.GrisModerno
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import kotlin.math.roundToInt
import kotlin.math.roundToLong

private val AccentSoft = Color(0xFFFFF6D8)
private val SuccessSoft = Color(0xFFEFFDF5)
private val DangerSoft = Color(0xFFFFF1F2)
private val NeutralSoft = Color(0xFFF5F6F8)
private val Danger = Color(0xFFEF4444)

fun mapStatsToChartDataGamificada(
    stats: SalesStatsResponse,
    selectedPeriod: Int
): List<Pair<String, Double>> {
    return when (selectedPeriod) {
        0 -> listOf("Hoy" to (stats.today ?: 0.0))

        1 -> {
            val week = stats.lastWeek
            listOf(
                "LU" to (week?.lunes ?: 0.0),
                "MA" to (week?.martes ?: 0.0),
                "MI" to (week?.miercoles ?: 0.0),
                "JUE" to (week?.jueves ?: 0.0),
                "VIE" to (week?.viernes ?: 0.0),
                "SAB" to (week?.sabado ?: 0.0),
                "DOM" to (week?.domingo ?: 0.0)
            )
        }

        2 -> {
            val currentMonth = stats.currentMonth?.monthIndex
            val month = stats.months.firstOrNull { it.monthIndex == currentMonth }
                ?: stats.months.maxByOrNull { it.monthIndex }

            val weeksByNumber = month?.weeks.orEmpty().associateBy { it.week }

            listOf(
                "S1" to (weeksByNumber[1]?.monto ?: 0.0),
                "S2" to (weeksByNumber[2]?.monto ?: 0.0),
                "S3" to (weeksByNumber[3]?.monto ?: 0.0),
                "S4" to (weeksByNumber[4]?.monto ?: 0.0)
            )
        }

        else -> {
            val monthLabels = listOf(
                1 to "Ene",
                2 to "Feb",
                3 to "Mar",
                4 to "Abr",
                5 to "May",
                6 to "Jun",
                7 to "Jul",
                8 to "Ago",
                9 to "Sep",
                10 to "Oct",
                11 to "Nov",
                12 to "Dic"
            )

            val monthsByIndex = stats.months.associateBy { it.monthIndex }

            monthLabels.map { (index, label) ->
                label to (monthsByIndex[index]?.total ?: 0.0)
            }
        }
    }
}

@Composable
fun BarChartGamificado(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    growthByLabel: Map<String, Double> = emptyMap()
) {
    val safeData = if (data.isEmpty()) listOf("Sin datos" to 0.0) else data
    val maxRaw = safeData.maxOfOrNull { it.second } ?: 0.0
    val max = if (maxRaw <= 0.0) 1.0 else maxRaw

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        safeData.forEachIndexed { index, (label, rawValue) ->
            val value = rawValue.takeIf { it.isFinite() } ?: 0.0
            val color = chartColors[index % chartColors.size]
            val ratio = (value / max).toFloat().coerceIn(0f, 1f)
            val growth = growthByLabel[label]

            val animatedHeight = remember(label) { Animatable(0f) }

            LaunchedEffect(label, ratio) {
                animatedHeight.snapTo(0f)
                animatedHeight.animateTo(
                    targetValue = ratio,
                    animationSpec = tween(800)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(48.dp)
            ) {
                Text(
                    text = formatChartMoney(value),
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 1
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .width(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray.copy(0.20f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(animatedHeight.value)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        color.copy(alpha = 0.92f),
                                        color.copy(alpha = 0.62f)
                                    )
                                )
                            )
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    label,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1
                )

                if (growth != null) {
                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "${if (growth >= 0) "+" else ""}${growth.roundToInt()}%",
                        fontSize = 9.sp,
                        color = if (growth >= 0) Cyan else Danger,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun formatChartMoney(value: Double): String {
    val safeValue = value.takeIf { it.isFinite() } ?: 0.0

    return when {
        safeValue >= 1_000_000 -> "$ ${(safeValue / 1_000_000).formatOneDecimal()}M"
        safeValue >= 1_000 -> "$ ${(safeValue / 1_000).roundToInt()}K"
        else -> "$ ${safeValue.roundToInt()}"
    }
}

private fun Double.formatOneDecimal(): String {
    val rounded = kotlin.math.round(this * 10) / 10
    return if (rounded % 1.0 == 0.0) {
        rounded.roundToInt().toString()
    } else {
        rounded.toString()
    }
}
@Composable
fun FireCleanCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                clip = false
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGrisSoftCard
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            content()
        }
    }
}

@Composable
fun FireMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    valueExtra: String? = null
) {
    FireCleanCard(modifier = modifier.heightIn(min = 104.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    value,
                    fontWeight = FontWeight.Normal,
                    fontSize = 17.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!valueExtra.isNullOrBlank()) {
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = valueExtra,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            FireIconBubble(
                icon = icon,
                background = Color(0xFFDFFBF0),
                tint = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun FireCurrentMonthCard(
    modifier: Modifier = Modifier,
    name: String,
    total: Double,
    cantidadVentas: Int,
    growthPercentage: Double?
) {
    val growth = growthPercentage ?: 0.0
    val isPositive = growth >= 0

    FireCleanCard(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFFF7E8),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FireIconBubble(
                    icon = Icons.Outlined.Today,
                    background = AccentSoft,
                    tint = AccentText,
                    size = 46.dp,
                    iconSize = 24.dp
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Mes actual",
                        fontSize = 12.sp,
                        color = AccentText,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        name,
                        fontSize = 20.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        "$cantidadVentas ventas · ${formatPercentage(growth)} vs mes anterior",
                        fontSize = 11.sp,
                        color = if (isPositive) Cyan else Danger,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    "$ ${total.roundToInt()}",
                    fontSize = 18.sp,
                    color = AccentText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FireTodayComparisonCard(
    modifier: Modifier = Modifier,
    today: Double?,
    yesterday: Double?,
    percentage: Double?
) {
    val safeToday = today ?: 0.0
    val safeYesterday = yesterday ?: 0.0
    val safePercentage = percentage ?: 0.0

    val trendIcon = when {
        safePercentage > 0.0 -> Icons.Outlined.TrendingUp
        safePercentage < 0.0 -> Icons.Outlined.TrendingDown
        else -> Icons.Outlined.TrendingFlat
    }

    val trendColor = when {
        safePercentage > 0.0 -> Cyan
        safePercentage < 0.0 -> Danger
        else -> TextSecondary
    }

    val trendBackground = when {
        safePercentage > 0.0 -> SuccessSoft
        safePercentage < 0.0 -> DangerSoft
        else -> NeutralSoft
    }

    FireCleanCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireIconBubble(
                icon = trendIcon,
                background = trendBackground,
                tint = trendColor,
                size = 48.dp,
                iconSize = 25.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Comparativa diaria",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "${formatPercentage(safePercentage)} vs ayer",
                    fontSize = 21.sp,
                    color = trendColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    "Hoy $ ${safeToday.roundToInt()} · Ayer $ ${safeYesterday.roundToInt()}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun FireMainPaymentMethodCard(
    modifier: Modifier = Modifier,
    method: PaymentMethodStats
) {
    FireCleanCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireIconBubble(
                icon = Icons.Outlined.Payments,
                background = AccentSoft,
                tint = AccentText,
                size = 48.dp,
                iconSize = 24.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Método principal",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    method.paymentMethod,
                    fontSize = 20.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    "${method.cantidadVentas} ventas",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Text(
                "$ ${method.totalVendido.roundToInt()}",
                fontSize = 17.sp,
                color = AccentText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FireBestProductHighlightCard(
    modifier: Modifier = Modifier,
    product: TopProductStats
) {
    FireCleanCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireIconBubble(
                icon = Icons.Outlined.ShoppingBag,
                background = AccentSoft,
                tint = AccentText,
                size = 48.dp,
                iconSize = 24.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Producto estrella",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    product.name,
                    fontSize = 20.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    "${product.cantidadTotal} unidades · $ ${product.price.roundToInt()} c/u",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                "$ ${product.totalVendido.roundToInt()}",
                fontSize = 17.sp,
                color = AccentText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FireBestMonthCard(
    modifier: Modifier = Modifier,
    name: String,
    total: Double
) {
    FireCleanCard(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            AccentSoft,
                            Color(0xFFFFFBEE)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireIconBubble(
                    icon = Icons.Outlined.Bolt,
                    background = Color.White,
                    tint = AccentText,
                    size = 46.dp,
                    iconSize = 24.dp
                )

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Mejor mes del año",
                        fontSize = 12.sp,
                        color = AccentText,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        name,
                        fontSize = 20.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "$ ${total.roundToInt()}",
                    fontSize = 18.sp,
                    color = AccentText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FirePaymentMethodsCard(
    modifier: Modifier = Modifier,
    paymentMethods: List<PaymentMethodStats>
) {
    FireCleanCard(modifier = modifier) {
        Text(
            "💳 Métodos de pago",
            fontWeight = FontWeight.Bold,
            color = GrisModerno
        )

        Spacer(Modifier.height(14.dp))

        val max = paymentMethods.maxOfOrNull { it.totalVendido } ?: 1.0

        paymentMethods.forEachIndexed { index, method ->
            val progress = (method.totalVendido / max).toFloat()
            val color = chartColors[index % chartColors.size]
            val animatedProgress = remember(method.paymentMethod) { Animatable(0f) }

            LaunchedEffect(method.paymentMethod, progress) {
                animatedProgress.snapTo(0f)
                animatedProgress.animateTo(progress, tween(850))
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            method.paymentMethod,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            "${method.cantidadVentas} ventas",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        "$ ${method.totalVendido.roundToInt()}",
                        color = GrisModerno,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(7.dp))

                FireProgressBarLine(
                    progress = animatedProgress.value,
                    startColor = color,
                    endColor = color.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
fun FireTopProductsCard(
    modifier: Modifier = Modifier,
    topProducts: List<TopProductStats>
) {
    FireCleanCard(modifier = modifier) {
        Text("🏆 Top productos", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(14.dp))

        val max = topProducts.maxOfOrNull { it.totalVendido } ?: 1.0

        topProducts.forEachIndexed { index, product ->
            val progress = (product.totalVendido / max).toFloat()
            val animatedProgress = remember(product.name) { Animatable(0f) }

            LaunchedEffect(product.name, progress) {
                animatedProgress.snapTo(0f)
                animatedProgress.animateTo(
                    targetValue = progress,
                    animationSpec = tween(900)
                )
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            product.name,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            "${product.cantidadTotal} unidades · $ ${product.price.roundToInt()} c/u",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

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
                    Box(
                        Modifier
                            .fillMaxWidth(animatedProgress.value.coerceIn(0f, 1f))
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

                    Text(
                        text = "${product.cantidadTotal} u.",
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

@Composable
fun FireSalesmanRankCard(
    modifier: Modifier = Modifier,
    salesmen: List<SalesmanRankStats>
) {
    FireCleanCard(modifier = modifier) {
        Text(
            "🚀 Top vendedores",
            fontWeight = FontWeight.Bold,
            color = GrisModerno
        )

        Spacer(Modifier.height(14.dp))

        val max = salesmen.maxOfOrNull { it.totalVendido } ?: 1.0

        salesmen.forEachIndexed { index, salesman ->
            val progress = (salesman.totalVendido / max).toFloat()
            val animatedProgress = remember(salesman.rank) { Animatable(0f) }

            LaunchedEffect(salesman.rank, progress) {
                animatedProgress.snapTo(0f)
                animatedProgress.animateTo(progress, tween(900))
            }

            val name = salesman.salesman?.username
                ?: salesman.salesman?.email
                ?: "Vendedor"

            Column(modifier = Modifier.padding(vertical = 9.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "#${salesman.rank}",
                            color = AccentText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            name,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            "${salesman.cantidadVentas} ventas · ${salesman.cantidadItems} items · ticket $ ${salesman.ticketPromedio.roundToInt()}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        "$ ${salesman.totalVendido.roundToInt()}",
                        color = GrisModerno,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                FireProgressBarLine(
                    progress = animatedProgress.value,
                    startColor = chartColors[index % chartColors.size],
                    endColor = chartColors[index % chartColors.size].copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
private fun FireIconBubble(
    icon: ImageVector,
    background: Color = AccentSoft,
    tint: Color = AccentText,
    size: androidx.compose.ui.unit.Dp = 38.dp,
    iconSize: androidx.compose.ui.unit.Dp = 20.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun FireProgressBarLine(
    progress: Float,
    startColor: Color,
    endColor: Color
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(0.25f))
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        listOf(startColor.copy(alpha = 0.9f), endColor)
                    )
                )
        )
    }
}

private fun formatPercentage(value: Double): String {
    val sign = if (value >= 0.0) "+" else ""
    return "$sign${value.roundToInt()}%"
}