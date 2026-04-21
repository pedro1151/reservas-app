package com.optic.pramosreservasappz.presentation.screens.rapidsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
@Composable
fun RapidProductCard(
    product: ProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<ProductResponse, Int>?,
    modifier: Modifier,
    viewModel: SalesViewModel
) {

    val quantity = inCart?.second ?: 0

    // 🎨 NUEVA PALETA
    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Color(0xFF22C1C3).copy(alpha = 0.12f)

    val TextPrimary = Color(0xFF111827)
    val TextSecondary = Color(0xFF6B7280)
    val BorderSoft = Color(0xFFE5E7EB)

    var position by remember { mutableStateOf(Offset.Zero) }

    val offsetX by animateDpAsState(
        targetValue = if (quantity > 0) 0.dp else 0.dp,
        animationSpec = tween(190)
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1f else 0f,
        animationSpec = tween(250)
    )

    val shortName = remember(product.name) {
        product.name.take(6).uppercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .onGloballyPositioned {
                position = it.localToRoot(Offset.Zero)
            }
            .clickable {
                addProduct()

                viewModel.triggerFlyAnimation(
                    SalesViewModel.FlyAnimationData(
                        product = product,
                        startX = position.x,
                        startY = position.y
                    )
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB) // 🔥 más limpio
        ),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(
            width = 0.6.dp,
            color = BorderSoft
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .offset(x = offsetX),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🟪 CAJA
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .border(
                                1.dp,
                                if (quantity > 0) Cyan else BorderSoft,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = shortName,
                            color = if (quantity > 0) Cyan else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 🔥 BADGE
                    if (quantity > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(25.dp)
                                .graphicsLayer {
                                    scaleX = badgeScale
                                    scaleY = badgeScale
                                }
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            Color(0xFF22C1C3),
                                            Color(0xFF4ADEDE)
                                        )
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = product.name,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            // 💰 PRECIO
            Text(
                text = "$ ${product.price}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = TextPrimary
            )

            // ❌ REMOVE
            // ❌ REMOVE (con animación)
            AnimatedVisibility(
                visible = quantity > 0,
                enter = fadeIn(tween(180)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(200)
                ),
                exit = fadeOut(tween(120)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(150)
                )
            ) {
                Spacer(Modifier.width(10.dp))

                val interaction = remember { MutableInteractionSource() }
                val pressed by interaction.collectIsPressedAsState()

                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.85f else 1f,
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable(
                            interactionSource = interaction,
                            indication = null
                        ) {
                            repeat(quantity) { removeProduct() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Quitar",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
