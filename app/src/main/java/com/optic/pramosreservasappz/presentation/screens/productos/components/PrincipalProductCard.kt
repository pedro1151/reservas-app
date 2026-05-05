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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.util.getAvatarColor
import kotlinx.coroutines.launch
import kotlin.math.abs

private val Red500 = Color(0xFFEF4444)
private val ACTION_WIDTH = 64.dp

@Composable
fun PrincipalProducCard(
    product: MiniProductResponse,
    navController: NavHostController,
    onDelete: (MiniProductResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val animatable = remember { Animatable(0f) }
    val offsetX = animatable.value
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showOptionsSheet by remember { mutableStateOf(false) }
    var isHidden by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(20L * (product.id % 10))
        visible = true
    }

    val priceText = remember(product.price) {
        try {
            "Bs. %,.0f".format(product.price.toString().toDouble())
        } catch (e: Exception) {
            "Bs. ${product.price}"
        }
    }

    val maxSwipe = -(ACTION_WIDTH.value * 2f)
    val editThreshold = -(ACTION_WIDTH.value * 0.7f)
    val deleteThreshold = -(ACTION_WIDTH.value * 1.4f)
    val swipeProgress = (-offsetX / abs(maxSwipe)).coerceIn(0f, 1f)

    fun goToDetail() {
        navController.navigate(
            ClientScreen.ProductDetail.createRoute(productId = product.id)
        )
    }

    fun goToEdit() {
        navController.navigate(
            ClientScreen.ABMProduct.createRoute(
                productId = product.id,
                editable = true
            )
        )
    }

    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = if (target == 0f) {
                        Spring.DampingRatioMediumBouncy
                    } else {
                        Spring.DampingRatioLowBouncy
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
        enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
        exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {
            val bgColor = when {
                offsetX <= deleteThreshold -> Color(0xFFFEF2F2)
                    .copy(alpha = 0.6f + swipeProgress * 0.4f)

                offsetX <= editThreshold -> primary
                    .copy(alpha = 0.08f + swipeProgress * 0.08f)

                else -> Color.Transparent
            }

            if (offsetX < -1f) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .matchParentSize()
                        .clip(RoundedCornerShape(20.dp))
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
                                    goToEdit()
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
                                .clip(RoundedCornerShape(14.dp))
                                .background(primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
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
                                if (offsetX <= deleteThreshold) {
                                    showDeleteDialog = true
                                }
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
                                .clip(RoundedCornerShape(14.dp))
                                .background(Red500),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX.dp)
                    .graphicsLayer {
                        rotationZ = (offsetX * 0.008f).coerceIn(-1.2f, 0f)
                        scaleY = (1f - abs(offsetX) / 1600f).coerceIn(0.975f, 1f)
                    }
                    .shadow(
                        elevation = if (offsetX > -1f) 4.dp else 0.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor = Color.Black.copy(alpha = 0.08f),
                        clip = false
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                isDragging = true
                            },
                            onHorizontalDrag = { _, drag ->
                                scope.launch {
                                    animatable.snapTo(
                                        (animatable.value + drag).coerceIn(maxSwipe, 0f)
                                    )
                                }
                            },
                            onDragEnd = {
                                val target = when {
                                    animatable.value <= deleteThreshold -> maxSwipe
                                    animatable.value <= editThreshold -> -ACTION_WIDTH.value
                                    else -> 0f
                                }

                                snap(target)

                                scope.launch {
                                    kotlinx.coroutines.delay(120)
                                    isDragging = false
                                }
                            },
                            onDragCancel = {
                                snap(0f)

                                scope.launch {
                                    kotlinx.coroutines.delay(120)
                                    isDragging = false
                                }
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (isDragging) return@clickable

                        if (offsetX < -1f) {
                            snap(0f)
                        } else {
                            goToDetail()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(avatarColor.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint = avatarColor.copy(alpha = 0.88f),
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            letterSpacing = (-0.2).sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(999.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Category,
                                    contentDescription = null,
                                    tint = TextSecondary.copy(alpha = 0.72f),
                                    modifier = Modifier.size(10.dp)
                                )

                                Text(
                                    text = product.type,
                                    fontSize = 10.sp,
                                    color = TextSecondary.copy(alpha = 0.82f),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.2.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = priceText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            letterSpacing = (-0.3).sp
                        )

                        if (offsetX > -1f) {
                            Spacer(Modifier.height(5.dp))

                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        showOptionsSheet = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.MoreVert,
                                    contentDescription = null,
                                    tint = BorderGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showOptionsSheet) {
        ServiceOptionsSheet(
            serviceName = product.name,
            isHidden = isHidden,
            onDismiss = { showOptionsSheet = false },
            onEdit = {
                showOptionsSheet = false
                goToEdit()
            },
            onPreview = {
                showOptionsSheet = false
                goToDetail()
            },
            onShare = { showOptionsSheet = false },
            onToggleHidden = { isHidden = it },
            onDuplicate = { showOptionsSheet = false },
            onDelete = {
                showOptionsSheet = false
                visible = false
                onDelete(product)
            },
            primary = primary
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                snap(0f)
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Red500,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "¿Borrar producto?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp
                )
            },
            text = {
                Text(
                    text = "Se eliminará \"${product.name}\". Esta acción no se puede deshacer.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        visible = false
                        onDelete(product)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Borrar producto",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        snap(0f)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancelar",
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServiceOptionsSheet(
    serviceName: String,
    isHidden: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onPreview: () -> Unit,
    onShare: () -> Unit,
    onToggleHidden: (Boolean) -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    primary: Color
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BorderGray)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Opciones",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = serviceName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.3).sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF8FAFC))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(Modifier.height(6.dp))

            OptionRow(
                icon = Icons.Outlined.Edit,
                label = "Editar producto",
                onClick = onEdit,
                tint = primary
            )

            OptionRow(
                icon = Icons.Outlined.Launch,
                label = "Vista previa",
                onClick = onPreview
            )

            Spacer(Modifier.height(6.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(Modifier.height(6.dp))

            OptionRow(
                icon = Icons.Outlined.DeleteOutline,
                label = "Borrar producto",
                onClick = onDelete,
                tint = Red500
            )
        }
    }
}

@Composable
private fun OptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = TextPrimary
) {
    val isDestructive = tint == Red500

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isDestructive) Color(0xFFFEF2F2)
                    else Color(0xFFF8FAFC)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = if (isDestructive) tint else TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}