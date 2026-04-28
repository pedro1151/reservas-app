package com.optic.pramosreservasappz.presentation.screens.newsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor

@Composable
fun RapidProductCard(
    product: ProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<ProductResponse, Int>?,
    modifier: Modifier,
    viewModel: NewSaleViewModel
) {

    val quantity = inCart?.second ?: 0

    // 🎨 PALETA
    val Primary = Color(0xFFE91E63)
    val PrimarySoft = Color(0xFFFCE4EC)

    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)
    val BorderNeutral = Color(0xFFE5E7EB)

    val Surface = Color.White
    val SurfaceSoft = Color(0xFFFFF7FA)

    val Danger = Color(0xFFE53935)
    val DangerSoft = Color(0xFFFFEBEE)

    var position by remember { mutableStateOf(Offset.Zero) }

    val offsetX by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(190),
        label = ""
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.58f,
            stiffness = 420f
        ),
        label = ""
    )

    // 🔥 REBOTE MODERNO
    val cardScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1.035f else 1f,
        animationSpec = spring(
            dampingRatio = 0.52f,
            stiffness = 340f
        ),
        label = ""
    )

    val shortName = remember(product.name) {
        product.name.take(6).uppercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
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
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(
            width = 0.8.dp,
            color = if (quantity > 0) PrimarySoft else BorderNeutral
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = offsetX),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (quantity > 0) TextPrimary else SurfaceSoft
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (quantity > 0)
                                        MaterialTheme.colorScheme.surface
                                    else BorderNeutral,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = shortName,
                                color = if (quantity > 0)
                                    MaterialTheme.colorScheme.background
                                else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 🔥 BADGE COUNT
                        if (quantity > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .graphicsLayer {
                                        scaleX = badgeScale
                                        scaleY = badgeScale
                                    }
                                    .background(
                                        color = ButtonSucessColor,
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = quantity.toString(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "$ ${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Primary
                )

                AnimatedVisibility(
                    visible = quantity > 0,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {

                    Spacer(Modifier.width(10.dp))

                    val interaction = remember { MutableInteractionSource() }
                    val pressed by interaction.collectIsPressedAsState()

                    val removeScale by animateFloatAsState(
                        targetValue = if (pressed) 0.88f else 1f,
                        animationSpec = tween(120),
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .graphicsLayer {
                                scaleX = removeScale
                                scaleY = removeScale
                            }
                            .clip(CircleShape)
                            .background(DangerSoft)
                            .clickable(
                                interactionSource = interaction,
                                indication = null
                            ) {
                                repeat(quantity) {
                                    removeProduct()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Quitar",
                            tint = Danger,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        if (quantity > 0) Primary else PrimarySoft
                    )
            )
        }
    }
}