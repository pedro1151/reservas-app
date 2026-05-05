package com.optic.pramosreservasappz.presentation.screens.newsale.components

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.util.getAvatarColor
import kotlinx.coroutines.delay

@Composable
fun RapidProductGridCard(
    product: MiniProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<MiniProductResponse, Int>?,
    modifier: Modifier = Modifier,
    viewModel: NewSaleViewModel
) {
    val quantity = inCart?.second ?: 0
    val isSelected = quantity > 0

    val primary = MaterialTheme.colorScheme.primary
    val surface = Color.White
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }

    val selectedSurface = Color(0xFFFFFBFD)
    val selectedLine = ButtonSucessColor.copy(alpha = 0.86f)

    val dangerSoft = Color(0xFFFFEBEE)
    val danger = Color(0xFFE53935)

    var position by remember { mutableStateOf(Offset.Zero) }

    val selectedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
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
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(),
        label = "badgeScale"
    )

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
        product.name
            .trim()
            .take(2)
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

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) selectedSurface else surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .graphicsLayer {
                scaleX = finalScale
                scaleY = finalScale
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
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSelected) selectedSurface else surface)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .background(avatarColor.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(21.dp))
                            .background(Color.White.copy(alpha = 0.88f))
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(21.dp),
                                ambientColor = Color.Black.copy(alpha = 0.03f),
                                spotColor = Color.Black.copy(alpha = 0.07f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = avatarColor.copy(alpha = 0.88f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .size(26.dp)
                                .graphicsLayer {
                                    scaleX = badgeScale
                                    scaleY = badgeScale
                                }
                                .clip(RoundedCornerShape(9.dp))
                                .background(ButtonSucessColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 11.dp,
                            bottom = if (isSelected) 34.dp else 11.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = product.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        letterSpacing = (-0.1).sp
                    )

                    Spacer(Modifier.height(7.dp))

                    Text(
                        text = priceText,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.5.sp,
                        letterSpacing = (-0.2).sp
                    )
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

            this@Card.AnimatedVisibility(
                visible = isSelected,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 9.dp),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(34.dp)
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
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            removePressed = true
                            repeat(quantity) {
                                removeProduct()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        tint = danger,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}