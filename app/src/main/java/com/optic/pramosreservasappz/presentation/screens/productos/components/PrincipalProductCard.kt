package com.optic.pramosreservasappz.presentation.screens.productos.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
    LaunchedEffect(Unit) { visible = true }

    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val maxSwipe        = -(ACTION_WIDTH.value * 2)
    val editThreshold   = -(ACTION_WIDTH.value * 0.8f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.5f)

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = if (target == 0f) Spring.DampingRatioNoBouncy
                    else              Spring.DampingRatioMediumBouncy,
                    stiffness    = Spring.StiffnessMedium
                )
            )
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(250)) + expandVertically(tween(250)),
        exit    = fadeOut(tween(180)) + shrinkVertically(tween(180))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    enabled           = offsetX < -1f
                ) { snap(0f) }
        ) {

            // ── Acciones de fondo: fondo blanco, íconos de color ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // EDITAR — ícono azul
                if (offsetX <= editThreshold) {
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable {
                                snap(0f)
                                navController.navigate(
                                    ClientScreen.ABMServicio.createRoute(
                                        serviceId = product.id,
                                        editable  = true
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint     = Color(0xFF2196F3),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Editar",
                                fontSize   = 11.sp,
                                color      = Color(0xFF2196F3),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // ELIMINAR — ícono rojo
                if (offsetX <= deleteThreshold) {
                    Box(
                        modifier = Modifier
                            .width(ACTION_WIDTH)
                            .fillMaxHeight()
                            .clickable { showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint     = Color(0xFFE53935),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Eliminar",
                                fontSize   = 11.sp,
                                color      = Color(0xFFE53935),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // ── Card principal ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        scaleY = (1f - abs(offsetX) / 1200f).coerceIn(0.98f, 1f)
                    }
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
                shape = if (offsetX < -2f)
                    RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                else
                    RoundedCornerShape(14.dp),
                color           = Color.White,
                shadowElevation = 0.dp,
                border          = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier         = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text          = getServiceInitials(product.name),
                            fontSize      = 13.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text          = product.name,
                            fontSize      = 14.sp,
                            fontWeight    = FontWeight.Medium,
                            color         = Color.Black,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text     = product.price.toString(),
                            fontSize = 12.sp,
                            color    = Color(0xFFAAAAAA)
                        )
                    }

                    if (offsetX > -1f) {
                        Box(
                            modifier         = Modifier
                                .size(32.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication        = null
                                ) { showOptionsSheet = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert, null,
                                tint     = Color(0xFFBBBBBB),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
            title = { Text("¿Borrar producto?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
            text  = {
                Text(
                    "Se eliminará \"${product.name}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp, color = Color(0xFF555555)
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; visible = false; onDelete(product) },
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape   = RoundedCornerShape(10.dp)
                ) { Text("Borrar", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; snap(0f) }) {
                    Text("Cancelar", color = Color.Black)
                }
            },
            shape          = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

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
                    color      = Color.Black,
                    modifier   = Modifier.weight(1f),
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Outlined.Close, null, tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))
            OptionRow(icon = Icons.Outlined.Edit,          label = "Editar",       onClick = onEdit)
            OptionRow(icon = Icons.Outlined.Launch,        label = "Vista previa", onClick = onPreview)
            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5),   thickness = 0.5.dp)
            Spacer(Modifier.height(4.dp))
            OptionRow(icon = Icons.Outlined.DeleteOutline, label = "Borrar",       onClick = onDelete, tint = Color(0xFFE53935))
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
        Text(label, fontSize = 15.sp, color = if (tint == Color(0xFF555555)) Color.Black else tint)
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
