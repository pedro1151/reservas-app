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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
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
    val isSelected = quantity > 0

    val Primary = Color(0xFFE91E63)
    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)

    val Surface = Color.White
    val SurfaceSoft = Color(0xFFF8FAFC)
    val SelectedSurface = Color(0xFFFFFBFD)
    val SelectedLine = ButtonSucessColor.copy(alpha = 0.86f)

    val Danger = Color(0xFFE53935)
    val DangerSoft = Color(0xFFFFEBEE)

    var position by remember { mutableStateOf(Offset.Zero) }

    val badgeScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.58f,
            stiffness = 420f
        ),
        label = "badgeScale"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isSelected) 1.018f else 1f,
        animationSpec = spring(
            dampingRatio = 0.52f,
            stiffness = 340f
        ),
        label = "cardScale"
    )

    val shortName = remember(product.name) {
        product.name
            .take(6)
            .uppercase()
    }

    val removeInteraction = remember { MutableInteractionSource() }
    val pressed by removeInteraction.collectIsPressedAsState()

    val removeScale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else 1f,
        animationSpec = tween(120),
        label = "removeScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = if (isSelected) 10.dp else 4.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = if (isSelected) 0.09f else 0.05f),
                spotColor = Color.Black.copy(alpha = if (isSelected) 0.14f else 0.08f)
            )
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
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSelected) SelectedSurface else Surface)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(58.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                if (isSelected) TextPrimary
                                else SurfaceSoft
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = shortName,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    this@Row.AnimatedVisibility(
                        visible = isSelected,
                        modifier = Modifier.align(Alignment.TopEnd),
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Box(
                            modifier = Modifier
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

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "$ ${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Primary,
                    maxLines = 1
                )

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier.size(34.dp),
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(tween(120)) + scaleIn(),
                        exit = fadeOut(tween(120)) + scaleOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .graphicsLayer {
                                    scaleX = removeScale
                                    scaleY = removeScale
                                }
                                .clip(CircleShape)
                                .background(DangerSoft)
                                .border(
                                    width = 1.dp,
                                    color = Danger.copy(alpha = 0.10f),
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = removeInteraction,
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
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        if (isSelected) SelectedLine
                        else Color.Transparent
                    )
            )
        }
    }
}