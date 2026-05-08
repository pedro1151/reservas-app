package com.optic.pramosreservasappz.presentation.screens.productos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.util.getAvatarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val GRID_ACTION_WIDTH = 72.dp
private val GRID_RED          = Color(0xFFEF4444)

@Composable
fun ProductGridCard(
    product      : MiniProductResponse,
    modifier     : Modifier = Modifier,
    navController: NavHostController,
    onDelete     : (MiniProductResponse) -> Unit,
    isLeftColumn : Boolean
) {
    val primary     = MaterialTheme.colorScheme.primary
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val animatable = remember { Animatable(0f) }
    val offsetX    = animatable.value
    val scope      = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var visible          by remember { mutableStateOf(false) }
    var isDragging       by remember { mutableStateOf(false) }

    val priceText = remember(product.price) {
        try { "$ %,.0f".format(product.price.toString().toDouble()) }
        catch (_: Exception) { "$ ${product.price}" }
    }

    val maxSwipe      = if (isLeftColumn) -GRID_ACTION_WIDTH.value else GRID_ACTION_WIDTH.value
    val snapThreshold = GRID_ACTION_WIDTH.value * 0.38f

    val isSwiped   = if (isLeftColumn) offsetX < -2f           else offsetX > 2f
    val isCardOpen = if (isLeftColumn) offsetX < -snapThreshold else offsetX > snapThreshold

    val overlayAlpha by animateFloatAsState(
        targetValue   = if (isSwiped) 0.06f else 0f,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label         = "overlay"
    )

    fun goToDetail() {
        navController.navigate(ClientScreen.ProductDetail.createRoute(productId = product.id))
    }

    fun goToEdit() {
        navController.navigate(
            ClientScreen.ABMProduct.createRoute(productId = product.id, editable = true)
        )
    }

    // ✅ CORRECCIÓN 1: DampingRatioNoBouncy + StiffnessHigh al abrir → sin rebote
    fun snap(target: Float) {
        scope.launch {
            animatable.animateTo(
                targetValue   = target,
                animationSpec = spring(
                    dampingRatio = if (target == 0f) Spring.DampingRatioMediumBouncy
                    else              Spring.DampingRatioNoBouncy,
                    stiffness    = if (target == 0f) Spring.StiffnessMedium
                    else              Spring.StiffnessHigh
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(24L * (product.id % 8))
        visible = true
    }

    AnimatedVisibility(
        visible  = visible,
        enter    = fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.96f),
        exit     = fadeOut(tween(180)) + scaleOut(tween(180), targetScale = 0.96f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
        ) {

            // ── Fondo blanco + botones estáticos detrás ──
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .align(
                            if (isLeftColumn) Alignment.CenterEnd
                            else              Alignment.CenterStart
                        )
                        .width(GRID_ACTION_WIDTH)
                        .fillMaxHeight(),
                    verticalArrangement   = Arrangement.Center,
                    horizontalAlignment   = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .border(1.5.dp, primary, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) { if (isCardOpen) { snap(0f); goToEdit() } },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint               = primary,
                            modifier           = Modifier.size(19.dp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) { if (isCardOpen) showDeleteDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint               = Color.White,
                            modifier           = Modifier.size(19.dp)
                        )
                    }
                }
            }

            // ── Card principal (deslizable) ──
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = offsetX.dp)
                    .shadow(
                        elevation    = if (!isSwiped) 8.dp else 2.dp,
                        shape        = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor    = Color.Black.copy(alpha = 0.08f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isDragging = true },
                            onHorizontalDrag = { _, drag ->
                                scope.launch {
                                    val current   = animatable.value
                                    val nearLimit = if (isLeftColumn)
                                        current < maxSwipe * 0.88f
                                    else
                                        current > maxSwipe * 0.88f
                                    val resistance = if (nearLimit) 0.25f else 1f
                                    val next = (current + drag * resistance).let {
                                        if (isLeftColumn) it.coerceIn(maxSwipe, 0f)
                                        else              it.coerceIn(0f, maxSwipe)
                                    }
                                    animatable.snapTo(next)
                                }
                            },
                            onDragEnd = {
                                snap(if (isCardOpen) maxSwipe else 0f)
                                scope.launch { delay(100); isDragging = false }
                            },
                            onDragCancel = {
                                snap(0f)
                                scope.launch { delay(100); isDragging = false }
                            }
                        )
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        if (isDragging) return@clickable
                        if (isSwiped) snap(0f) else goToDetail()
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // ── Área superior con icono ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(avatarColor.copy(alpha = 0.08f))
                    ) {
                        // ✅ CORRECCIÓN 2: flecha en TopEnd para col izq, TopStart para col der
                        Box(
                            modifier = Modifier
                                .align(
                                    if (isLeftColumn) Alignment.TopEnd
                                    else              Alignment.TopStart
                                )
                                .padding(10.dp)
                                .size(28.dp)
                                .shadow(
                                    elevation    = 4.dp,
                                    shape        = CircleShape,
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor    = Color.Black.copy(alpha = 0.08f)
                                )
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.92f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isLeftColumn)
                                    Icons.Default.KeyboardArrowLeft
                                else
                                    Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint               = TextSecondary,
                                modifier           = Modifier.size(14.dp)
                            )
                        }

                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(21.dp))
                                    .background(Color.White.copy(alpha = 0.88f))
                                    .shadow(
                                        elevation    = 5.dp,
                                        shape        = RoundedCornerShape(21.dp),
                                        ambientColor = Color.Black.copy(alpha = 0.03f),
                                        spotColor    = Color.Black.copy(alpha = 0.07f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector        = Icons.Outlined.Inventory2,
                                    contentDescription = null,
                                    tint               = avatarColor.copy(alpha = 0.88f),
                                    modifier           = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // ── Área inferior con texto ──
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                start   = 12.dp,
                                end     = 12.dp,
                                top     = 11.dp,
                                bottom  = 11.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text          = product.name,
                            fontSize      = 13.5.sp,
                            fontWeight    = FontWeight.SemiBold,
                            color         = TextPrimary,
                            maxLines      = 2,
                            overflow      = TextOverflow.Ellipsis,
                            lineHeight    = 18.sp,
                            letterSpacing = (-0.1).sp,
                            textAlign     = TextAlign.Center
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text          = priceText,
                                fontSize      = 15.5.sp,
                                fontWeight    = FontWeight.SemiBold,
                                color         = TextSecondary,
                                letterSpacing = (-0.2).sp
                            )

                            Spacer(Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(999.dp))
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Category,
                                        contentDescription = null,
                                        tint               = TextSecondary.copy(alpha = 0.72f),
                                        modifier           = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text          = product.type,
                                        fontSize      = 10.sp,
                                        color         = TextSecondary.copy(alpha = 0.82f),
                                        fontWeight    = FontWeight.Bold,
                                        letterSpacing = 0.2.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Overlay al deslizar ──
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = overlayAlpha))
                )
            }
        }
    }

    // ── Dialog de confirmación de borrado ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false; snap(0f) },
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
                        tint               = GRID_RED,
                        modifier           = Modifier.size(22.dp)
                    )
                }
            },
            title = {
                Text(
                    text          = "¿Borrar producto?",
                    fontSize      = 17.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = TextPrimary,
                    letterSpacing = (-0.3).sp
                )
            },
            text = {
                Text(
                    text       = "Se eliminará \"${product.name}\". Esta acción no se puede deshacer.",
                    fontSize   = 14.sp,
                    color      = TextSecondary,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick  = { showDeleteDialog = false; visible = false; onDelete(product) },
                    colors   = ButtonDefaults.buttonColors(containerColor = GRID_RED),
                    shape    = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Borrar producto", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick  = { showDeleteDialog = false; snap(0f) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = TextSecondary, fontWeight = FontWeight.Medium)
                }
            },
            shape          = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}