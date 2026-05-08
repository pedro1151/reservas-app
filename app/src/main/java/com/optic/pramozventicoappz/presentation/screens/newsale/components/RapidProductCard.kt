package com.optic.pramozventicoappz.presentation.screens.newsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramozventicoappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary
import com.optic.pramozventicoappz.presentation.util.getAvatarColor

@Composable
fun RapidProductCard(
    product: MiniProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<MiniProductResponse, Int>?,
    modifier: Modifier,
    viewModel: NewSaleViewModel
) {
    val quantity = inCart?.second ?: 0
    val isSelected = quantity > 0

    val primary = MaterialTheme.colorScheme.primary
    val surface = Color.White
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val selectedSurface = Color(0xFFFFFBFD)
    val selectedLine = ButtonSucessColor.copy(alpha = 0.86f)

    val danger = Color(0xFFE53935)
    val dangerSoft = Color(0xFFFFEBEE)

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
            .trim()
            .take(6)
            .uppercase()
            .ifBlank { "PR" }
    }

    val priceText = remember(product.price) {
        try {
            "$ %,.0f".format(product.price.toString().toDouble())
        } catch (_: Exception) {
            "$ ${product.price}"
        }
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
            .height(74.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .shadow(
                elevation = if (isSelected) 10.dp else 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = if (isSelected) 0.06f else 0.04f),
                spotColor = Color.Black.copy(alpha = if (isSelected) 0.10f else 0.08f)
            )
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = if (isSelected)
                    primary.copy(alpha = 0.18f)
                else
                    Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .onGloballyPositioned {
                position = it.localToRoot(Offset.Zero)
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
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
            containerColor = if (isSelected) selectedSurface else surface
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSelected) selectedSurface else surface)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(21.dp))
                            .background(avatarColor.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.88f))
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.03f),
                                    spotColor = Color.Black.copy(alpha = 0.07f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = shortName,
                                color = avatarColor.copy(alpha = 0.88f),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }

                    this@Row.AnimatedVisibility(
                        visible = isSelected,
                        modifier = Modifier.align(Alignment.TopEnd),
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(23.dp)
                                .graphicsLayer {
                                    scaleX = badgeScale
                                    scaleY = badgeScale
                                }
                                .clip(RoundedCornerShape(8.dp))
                                .background(ButtonSucessColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
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
                        fontSize = 14.5.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.1).sp
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = priceText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.5.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    letterSpacing = (-0.2).sp
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
                                .background(dangerSoft)
                                .border(
                                    width = 1.dp,
                                    color = danger.copy(alpha = 0.10f),
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
                                tint = danger,
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
                        if (isSelected) selectedLine
                        else Color.Transparent
                    )
            )
        }
    }
}