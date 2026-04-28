package com.optic.pramosreservasappz.presentation.screens.newsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import kotlinx.coroutines.delay

@Composable
fun RapidProductGridCard(
    product: ProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<ProductResponse, Int>?,
    modifier: Modifier = Modifier,
    viewModel: NewSaleViewModel
) {

    val quantity = inCart?.second ?: 0

    val Primary = Color(0xFFE91E63)
    val PrimarySoft = Color(0xFFFCE4EC)

    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)

    val BorderNeutral = Color(0xFFE5E7EB)
    val Surface = Color.White
    val SurfaceSoft = Color(0xFFFFF7FA)

    val DangerSoft = Color(0xFFFFEBEE)
    val Danger = Color(0xFFE53935)

    var position by remember { mutableStateOf(Offset.Zero) }

    // 🔥 Animación general
    val selectedScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1.02f else 1f,
        animationSpec = tween(220),
        label = "selectedScale"
    )

    var pulseTrigger by remember { mutableStateOf(0) }
    var lastQuantity by remember { mutableStateOf(quantity) }

    LaunchedEffect(quantity) {
        if (quantity > lastQuantity) pulseTrigger++
        lastQuantity = quantity
    }

    val pulseScale by animateFloatAsState(
        targetValue = if (pulseTrigger > 0) 1.06f else 1f,
        animationSpec = spring(
            dampingRatio = 0.45f,
            stiffness = 420f
        ),
        label = "pulseScale"
    )

    LaunchedEffect(pulseTrigger) {
        if (pulseTrigger > 0) {
            delay(140)
            pulseTrigger = 0
        }
    }

    val finalScale = selectedScale * pulseScale

    val badgeScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1f else 0f,
        animationSpec = spring(),
        label = "badgeScale"
    )

    // 🔥 Animación del botón REMOVE
    var removePressed by remember { mutableStateOf(false) }

    val removeScale by animateFloatAsState(
        targetValue = if (removePressed) 0.82f else 1f,
        animationSpec = spring(
            dampingRatio = 0.45f,
            stiffness = 600f
        ),
        label = "removeScale"
    )

    LaunchedEffect(removePressed) {
        if (removePressed) {
            delay(100)
            removePressed = false
        }
    }

    val initials = remember(product.name) {
        product.name.take(2).uppercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .graphicsLayer {
                scaleX = finalScale
                scaleY = finalScale
            }
            .onGloballyPositioned {
                position = it.localToRoot(Offset.Zero)
            }
            .clickable {
                addProduct()

                viewModel.triggerFlyAnimation(
                    NewSaleViewModel.FlyAnimationData(
                        product = product,
                        startX = position.x,
                        startY = position.y
                    )
                )
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (quantity > 0) 1.2.dp else 1.dp,
            color = if (quantity > 0) PrimarySoft else BorderNeutral
        ),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (quantity > 0) 4.dp else 1.dp
        )
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {

                // 🔥 HEADER (sin cambios)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .background(
                            if (quantity > 0) {
                                Brush.horizontalGradient(
                                    listOf(
                                        Primary.copy(alpha = 0.12f),
                                        PrimarySoft
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(SurfaceSoft, Surface)
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                if (quantity > 0) TextPrimary
                                else Color(0xFFF3F4F6)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = if (quantity > 0) Color.White else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // 🔥 BADGE (MISMA POSICIÓN ORIGINAL)
                    if (quantity > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .size(25.dp)
                                .graphicsLayer {
                                    scaleX = badgeScale
                                    scaleY = badgeScale
                                }
                                .background(
                                    ButtonSucessColor,
                                    RoundedCornerShape(9.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // 🔥 BODY (SIN QUE AFECTE EL BOTÓN)
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = product.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "$ ${product.price}",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }

            // 🔥 BOTÓN REMOVE (FIJO, FUERA DEL FLOW)
            this@Card.AnimatedVisibility(
                visible = quantity > 0,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(36.dp) // 🔥 TAMAÑO FIJO
                        .graphicsLayer {
                            scaleX = removeScale
                            scaleY = removeScale
                        }
                        .clip(CircleShape)
                        .background(DangerSoft)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        ) {
                            removePressed = true

                            repeat(quantity) {
                                removeProduct() // 🔥 MANTENEMOS TU LÓGICA ORIGINAL
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = null,
                        tint = Danger,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}