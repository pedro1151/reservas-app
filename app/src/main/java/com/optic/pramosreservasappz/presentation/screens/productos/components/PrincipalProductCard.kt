package com.optic.pramosreservasappz.presentation.screens.productos.components

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.historial.components.getAvatarColor
import kotlinx.coroutines.launch
import kotlin.math.abs

private val ACTION_WIDTH = 64.dp

@Composable
fun PrincipalProducCard(
    product: ProductResponse,
    navController: NavHostController,
    onDelete: (ProductResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }
    val offsetX    = animatable.value
    val scope      = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showOptionsSheet by remember { mutableStateOf(false) }
    var isHidden         by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(20L * (product.id % 10))
        visible = true
    }

    val avatarColor = remember(product.id) { getAvatarColor(product.id) }
    val priceText   = remember(product.price) {
        try { "Bs. %,.0f".format(product.price.toString().toDouble()) }
        catch (e: Exception) { "Bs. ${product.price}" }
    }

    // Umbrales del swipe
    val maxSwipe        = -(ACTION_WIDTH.value * 2f)
    val editThreshold   = -(ACTION_WIDTH.value * 0.7f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.4f)

    // Progreso normalizado [0..1]
    val swipeProgress = (-offsetX / abs(maxSwipe)).coerceIn(0f, 1f)

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = when {
                        target == 0f -> Spring.DampingRatioMediumBouncy
                        else         -> Spring.DampingRatioLowBouncy
                    },
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    // Escala spring de los íconos de acción
    val editIconScale by animateFloatAsState(
        targetValue   = if (offsetX <= editThreshold) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label         = "editScale"
    )
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

            // ── Fondo de acciones con transición de color ──
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
                // ── Botón EDITAR ──
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
                                    ClientScreen.ABMServicio.createRoute(
                                        serviceId = product.id,
                                        editable  = true
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

                // ── Botón ELIMINAR ──
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
                        rotationZ       = (offsetX * 0.008f).coerceIn(-1.2f, 0f)
                        scaleY          = (1f - abs(offsetX) / 1600f).coerceIn(0.975f, 1f)
                        shadowElevation = (4f - swipeProgress * 4f).coerceAtLeast(0f)
                    }
                    .shadow(
                        elevation    = if (offsetX > -1f) 2.dp else 0.dp,
                        shape        = RoundedCornerShape(18.dp),
                        ambientColor = avatarColor.copy(alpha = 0.12f),
                        spotColor    = avatarColor.copy(alpha = 0.08f)
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
                            navController.navigate(
                                ClientScreen.ServiceDetail.createRoute(serviceId = product.id)
                            )
                        } else snap(0f)
                    },
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ── Franja de color izquierda ──
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .height(70.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(avatarColor, avatarColor.copy(alpha = 0.4f))
                                ),
                                RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)
                            )
                    )

                    Spacer(Modifier.width(12.dp))

                    // ── Avatar circular con iniciales ──
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(avatarColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = getServiceInitials(product.name),
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color      = avatarColor
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // ── Contenido central ──
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = product.name,
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
                                    .background(
                                        avatarColor.copy(alpha = 0.10f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Category, null,
                                    tint     = avatarColor,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    "Producto", fontSize = 10.sp, color = avatarColor,
                                    fontWeight = FontWeight.SemiBold, letterSpacing = 0.3.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    // ── Precio + MoreVert ──
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier            = Modifier.padding(end = 14.dp)
                    ) {
                        Text(
                            text          = priceText,
                            fontSize      = 13.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = avatarColor,
                            letterSpacing = (-0.3).sp
                        )
                        if (offsetX > -1f) {
                            Spacer(Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication        = null
                                    ) { showOptionsSheet = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.MoreVert, null,
                                    tint     = Color(0xFFCCCCCC),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Options Sheet ──
    if (showOptionsSheet) {
        ServiceOptionsSheet(
            serviceName    = product.name,
            isHidden       = isHidden,
            onDismiss      = { showOptionsSheet = false },
            onEdit         = { showOptionsSheet = false },
            onPreview      = { showOptionsSheet = false },
            onShare        = { showOptionsSheet = false },
            onToggleHidden = { isHidden = it },
            onDuplicate    = { showOptionsSheet = false },
            onDelete       = { showOptionsSheet = false; visible = false; onDelete(product) }
        )
    }

    // ── Diálogo de confirmación eliminación ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
            title = {
                Text(
                    "¿Borrar producto?", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E)
                )
            },
            text = {
                Text(
                    "Se eliminará \"${product.name}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp, color = Color(0xFF555577)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; visible = false; onDelete(product) },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFE05C5C)),
                    shape   = RoundedCornerShape(12.dp)
                ) { Text("Borrar", color = Color.White, fontWeight = FontWeight.SemiBold) }
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

// ── Bottom Sheet de opciones (sin cambios funcionales) ──
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceOptionsSheet(
    serviceName    : String,
    isHidden       : Boolean,
    onDismiss      : () -> Unit,
    onEdit         : () -> Unit,
    onPreview      : () -> Unit,
    onShare        : () -> Unit,
    onToggleHidden : (Boolean) -> Unit,
    onDuplicate    : () -> Unit,
    onDelete       : () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = Color.White,
        shape            = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle       = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = serviceName,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF1A1A2E),
                    modifier   = Modifier.weight(1f),
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        Icons.Outlined.Close, null,
                        tint     = Color(0xFF888888),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))
            OptionRow(icon = Icons.Outlined.Edit,          label = "Editar",       onClick = onEdit)
            OptionRow(icon = Icons.Outlined.Launch,        label = "Vista previa", onClick = onPreview)
            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))
            OptionRow(
                icon    = Icons.Outlined.DeleteOutline,
                label   = "Borrar",
                onClick = onDelete,
                tint    = Color(0xFFE05C5C)
            )
        }
    }
}

@Composable
private fun OptionRow(
    icon    : ImageVector,
    label   : String,
    onClick : () -> Unit,
    tint    : Color = Color(0xFF555555)
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick
            )
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(
            label,
            fontSize = 15.sp,
            color    = if (tint == Color(0xFF555555)) Color(0xFF1A1A2E) else tint
        )
    }
}

private fun getServiceInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}
