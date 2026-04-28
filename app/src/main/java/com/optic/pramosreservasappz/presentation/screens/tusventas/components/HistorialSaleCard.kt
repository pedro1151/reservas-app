package com.optic.pramosreservasappz.presentation.screens.tusventas.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import kotlinx.coroutines.launch
import kotlin.math.abs

private val ACTION_WIDTH = 64.dp

private val SALE_PALETTE = listOf(
    Color(0xFF6E4FDB), Color(0xFF3B78C4), Color(0xFF10A37F),
    Color(0xFFE05C5C), Color(0xFFD97706), Color(0xFF7C3AED),
    Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF16A34A),
    Color(0xFF9333EA), Color(0xFF2563EB), Color(0xFFDC6B19),
)

fun getAvatarColor(id: Int): Color = SALE_PALETTE[id % SALE_PALETTE.size]

private fun formatAmount(raw: Any?): String = try {
    "Bs. %,.0f".format(raw.toString().toDouble())
} catch (e: Exception) { "Bs. $raw" }

fun getInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

@Composable
fun HistorialSaleCard(
    sale: SaleResponse,
    navController: NavHostController,
    onDelete: (ClientResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }
    val offsetX    = animatable.value
    val scope      = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(20L * (sale.id % 10))
        visible = true
    }

    val accentColor = remember(sale.id) { getAvatarColor(sale.id) }
    val amountText  = remember(sale.amount) { formatAmount(sale.amount) }

    // Umbrales del swipe
    val maxSwipe        = -(ACTION_WIDTH.value * 2f)
    val editThreshold   = -(ACTION_WIDTH.value * 0.7f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.4f)

    // Progreso normalizado del swipe [0..1] para animar el fondo
    val swipeProgress = (-offsetX / abs(maxSwipe)).coerceIn(0f, 1f)

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = when {
                        target == 0f -> Spring.DampingRatioMediumBouncy
                        else         -> Spring.DampingRatioLowBouncy   // más rebote al abrir
                    },
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    // Escala animada del ícono editar
    val editIconScale by animateFloatAsState(
        targetValue   = if (offsetX <= editThreshold) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label         = "editScale"
    )
    // Escala animada del ícono eliminar
    val deleteIconScale by animateFloatAsState(
        targetValue   = if (offsetX <= deleteThreshold) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label         = "deleteScale"
    )

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
        exit    = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX < -1f
                ) { snap(0f) }
        ) {

            // ── Fondo de acciones ──
            // Color del fondo transiciona de blanco → azul (edit) → rojo (delete)
            val bgColor = when {
                offsetX <= deleteThreshold -> Color(0xFFFFEDED).copy(alpha = 0.5f + swipeProgress * 0.5f)
                offsetX <= editThreshold   -> Color(0xFFEAF2FF).copy(alpha = 0.5f + swipeProgress * 0.5f)
                else                       -> Color(0xFFF3F3F3)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(bgColor),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // ── Botón EDITAR (solo ícono, con escala spring) ──
                Box(
                    modifier = Modifier
                        .width(ACTION_WIDTH)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) {
                            if (offsetX <= editThreshold) {
                                snap(0f)
                                navController.navigate(
                                    ClientScreen.ABMCliente.createRoute(clientId = sale.id, editable = true)
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
                                alpha  = editIconScale
                            }
                            .clip(CircleShape)
                            .background(Color(0xFF3B78C4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // ── Botón ELIMINAR (solo ícono, con escala spring) ──
                Box(
                    modifier = Modifier
                        .width(ACTION_WIDTH)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
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
                                alpha  = deleteIconScale
                            }
                            .clip(CircleShape)
                            .background(Color(0xFFE05C5C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Card principal con tilt sutil durante drag ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        // Leve tilt durante el deslizamiento
                        rotationZ = (offsetX * 0.008f).coerceIn(-1.2f, 0f)
                        // Escala vertical sutil
                        scaleY = (1f - abs(offsetX) / 1600f).coerceIn(0.975f, 1f)
                        // Sombra se reduce al abrir
                        shadowElevation = (4f - swipeProgress * 4f).coerceAtLeast(0f)
                    }
                    .shadow(
                        elevation    = if (offsetX > -1f) 2.dp else 0.dp,
                        shape        = RoundedCornerShape(18.dp),
                        ambientColor = accentColor.copy(alpha = 0.12f),
                        spotColor    = accentColor.copy(alpha = 0.08f)
                    )
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                val target = when {
                                    animatable.value <= deleteThreshold -> maxSwipe
                                    animatable.value <= editThreshold   -> -ACTION_WIDTH.value
                                    else                                -> 0f
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
                        indication        = null
                    ) {
                        if (offsetX > -1f) {
                            navController.navigate(ClientScreen.SaleDetail.createRoute(saleId = sale.id))
                        } else snap(0f)
                    },
                shape = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Franja de color izquierda
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .height(70.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(accentColor, accentColor.copy(alpha = 0.4f))
                                ),
                                RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)
                            )
                    )

                    Spacer(Modifier.width(12.dp))

                    // Avatar circular
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "#${sale.id}".take(5),
                            fontSize   = if (sale.id > 99) 10.sp else 12.sp,
                            fontWeight = FontWeight.Bold,
                            color      = accentColor
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Contenido central
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = sale.description ?: "Venta #${sale.id}",
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Color(0xFF1A1A2E),
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.3).sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.10f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(Icons.Outlined.ShoppingBag, null,
                                    tint     = accentColor,
                                    modifier = Modifier.size(10.dp))
                                Text("Vendedor", fontSize = 10.sp, color = accentColor,
                                    fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp)
                            }
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    // Monto + chevron
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier            = Modifier.padding(end = 14.dp)
                    ) {
                        Text(
                            text          = amountText,
                            fontSize      = 13.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = accentColor,
                            letterSpacing = (-0.3).sp
                        )
                        if (offsetX > -1f) {
                            Spacer(Modifier.height(2.dp))
                            Icon(Icons.Outlined.ChevronRight, null,
                                tint     = Color(0xFFCCCCCC),
                                modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }

    // ── Diálogo de confirmación ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
            title = {
                Text("¿Eliminar venta?", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
            },
            text = {
                Text(
                    "Se eliminará \"${sale.description}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp, color = Color(0xFF555577)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; visible = false },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFE05C5C)),
                    shape   = RoundedCornerShape(12.dp)
                ) { Text("Eliminar", color = Color.White, fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; snap(0f) }) {
                    Text("Cancelar", color = Color(0xFF666688))
                }
            },
            shape          = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}
