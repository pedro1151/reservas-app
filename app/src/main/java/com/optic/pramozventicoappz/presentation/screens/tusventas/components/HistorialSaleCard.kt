package com.optic.pramozventicoappz.presentation.screens.tusventas.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.SaleResponse
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

private val ACTION_WIDTH = 64.dp

private val PrimaryPink = Color(0xFFE91E63)
private val PrimaryPinkDark = Color(0xFFD81B60)

private val TextPrimarySoft = Color(0xFF334155)
private val TextSecondarySoft = Color(0xFF64748B)
private val AmountSoft = Color(0xFF475569)
private val DividerGray = Color(0xFFE2E8F0)
private val SaleTagText = Color(0xFF8A3A55)
private val SaleTagBg = Color(0xFFF8F4F6)
private val BorderGray = Color(0xFFE5E7EB)

private fun formatAmount(raw: Any?): String = try {
    "\$ ${"%,.0f".format(raw.toString().toDouble())}"
} catch (e: Exception) {
    "\$ $raw"
}

private fun formatSalePedidoDate(rawDate: String): String {
    return try {
        val parsed = OffsetDateTime.parse(rawDate)
        val day = parsed.dayOfWeek.getDisplayName(
            java.time.format.TextStyle.SHORT,
            Locale("es", "ES")
        ).replace(".", "")

        val month = parsed.month.getDisplayName(
            java.time.format.TextStyle.SHORT,
            Locale("es", "ES")
        ).replace(".", "")

        "$day ${parsed.dayOfMonth} de $month · ${parsed.format(DateTimeFormatter.ofPattern("HH:mm"))} hs"
    } catch (e: Exception) {
        rawDate
    }
}

@Composable
fun HistorialSaleCard(
    sale: SaleResponse,
    navController: NavHostController,
    onDelete: (SaleResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }
    val offsetX = animatable.value
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(35L * (sale.id % 10))
        visible = true
    }

    val amountText = remember(sale.amount) { formatAmount(sale.amount) }
    val dateText = remember(sale.created) { formatSalePedidoDate(sale.created) }

    val title = sale.description ?: "Venta rápida"
    val clientName = sale.client?.fullName ?: "Cliente"
    val salesmanName = sale.salesman?.username ?: sale.salesman?.email ?: "Vendedor"

    val maxSwipe = -(ACTION_WIDTH.value * 2f)
    val editThreshold = -(ACTION_WIDTH.value * 0.7f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.4f)

    val swipeProgress = (-offsetX / abs(maxSwipe)).coerceIn(0f, 1f)

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = when {
                        target == 0f -> Spring.DampingRatioMediumBouncy
                        else -> Spring.DampingRatioLowBouncy
                    },
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    val editIconScale by animateFloatAsState(
        targetValue = if (offsetX <= editThreshold) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "editScale"
    )

    val deleteIconScale by animateFloatAsState(
        targetValue = if (offsetX <= deleteThreshold) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "deleteScale"
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(520)) + slideInVertically(tween(520)) { it / 5 },
        exit = fadeOut(tween(240)) + shrinkVertically(tween(240))
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = offsetX < -1f
                    ) { snap(0f) }
            ) {
                val bgColor = when {
                    offsetX <= deleteThreshold -> Color(0xFFFFEDED).copy(alpha = 0.5f + swipeProgress * 0.5f)
                    offsetX <= editThreshold -> Color(0xFFEAF2FF).copy(alpha = 0.5f + swipeProgress * 0.5f)
                    else -> Color.Transparent
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(108.dp)
                        .background(bgColor),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (offsetX <= editThreshold) {
                                    snap(0f)
                                    navController.navigate(
                                        ClientScreen.ABMCliente.createRoute(
                                            clientId = sale.id,
                                            editable = true
                                        )
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .graphicsLayer {
                                    scaleX = editIconScale
                                    scaleY = editIconScale
                                    alpha = editIconScale
                                }
                                .clip(CircleShape)
                                .background(Color(0xFF3B78C4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (offsetX <= deleteThreshold) showDeleteDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .graphicsLayer {
                                    scaleX = deleteIconScale
                                    scaleY = deleteIconScale
                                    alpha = deleteIconScale
                                }
                                .clip(CircleShape)
                                .background(Color(0xFFE05C5C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = offsetX.dp)
                        .graphicsLayer {
                            rotationZ = (offsetX * 0.008f).coerceIn(-1.2f, 0f)
                            scaleY = (1f - abs(offsetX) / 1600f).coerceIn(0.975f, 1f)
                        }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    val target = when {
                                        animatable.value <= deleteThreshold -> maxSwipe
                                        animatable.value <= editThreshold -> -ACTION_WIDTH.value
                                        else -> 0f
                                    }
                                    snap(target)
                                },
                                onHorizontalDrag = { _, drag ->
                                    scope.launch {
                                        animatable.snapTo(
                                            (animatable.value + drag).coerceIn(maxSwipe, 0f)
                                        )
                                    }
                                }
                            )
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (offsetX > -1f) {
                                navController.navigate(
                                    ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                                )
                            } else {
                                snap(0f)
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        PrimaryPink.copy(alpha = 0.88f),
                                        PrimaryPinkDark.copy(alpha = 0.82f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(SaleTagBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Venta",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SaleTagText.copy(alpha = 0.82f)
                                )
                            }

                            Spacer(Modifier.width(6.dp))

                            Text(
                                text = dateText,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextSecondarySoft,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(Modifier.height(5.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimarySoft.copy(alpha = 0.92f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                letterSpacing = (-0.20).sp,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = amountText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = AmountSoft.copy(alpha = 0.96f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "$clientName · $salesmanName",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextSecondarySoft.copy(alpha = 0.82f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(start = 88.dp, end = 16.dp),
                thickness = 1.15.dp,
                color = Color(0xFFCBD5E1)
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                snap(0f)
            },
            title = {
                Text(
                    text = "¿Eliminar venta?",
                    fontSize = 16.sp,
                    color = TextPrimarySoft
                )
            },
            text = {
                Text(
                    text = "Se eliminará \"${sale.description}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp,
                    color = TextSecondarySoft
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        visible = false
                        onDelete(sale)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE05C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        snap(0f)
                    }
                ) {
                    Text("Cancelar", color = TextSecondarySoft)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun HistorialSaleGridCard(
    sale: SaleResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onDelete: (SaleResponse) -> Unit
) {
    val amountText = remember(sale.amount) { formatAmount(sale.amount) }
    val title = sale.description ?: "Venta rápida"
    val clientName = sale.client?.fullName ?: "Cliente"
    val salesmanName = sale.salesman?.username ?: sale.salesman?.email ?: "Vendedor"

    var showMenu by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(42L * (sale.id % 8))
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(520)) + scaleIn(tween(520), initialScale = 0.965f),
        exit = fadeOut(tween(240)),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    navController.navigate(
                        ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                    )
                },
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.96f),
            shadowElevation = 1.dp,
            border = BorderStroke(1.dp, BorderGray.copy(alpha = 0.82f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        PrimaryPink.copy(alpha = 0.88f),
                                        PrimaryPinkDark.copy(alpha = 0.82f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF8FAFC))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    showMenu = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreVert,
                                contentDescription = "Más opciones",
                                tint = TextSecondarySoft,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Eliminar",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFE53935)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = null,
                                        tint = Color(0xFFE53935),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete(sale)
                                }
                            )
                        }
                    }
                }

                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(SaleTagBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Venta",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = SaleTagText.copy(alpha = 0.82f)
                        )
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        text = title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimarySoft.copy(alpha = 0.92f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = clientName,
                        fontSize = 11.sp,
                        color = TextSecondarySoft,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = salesmanName,
                        fontSize = 11.sp,
                        color = TextSecondarySoft.copy(alpha = 0.82f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = amountText,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = AmountSoft.copy(alpha = 0.96f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}