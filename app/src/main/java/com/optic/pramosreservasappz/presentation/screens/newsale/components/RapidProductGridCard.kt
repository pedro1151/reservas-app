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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
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
    val isSelected = quantity > 0

    val Primary = Color(0xFFE91E63)
    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)

    val Surface = Color.White
    val HeaderBg = Color(0xFFF8FAFC)
    val SelectedSurface = Color(0xFFFFFBFD)

    val SelectedLine = ButtonSucessColor.copy(alpha = 0.86f)

    val DangerSoft = Color(0xFFFFEBEE)
    val Danger = Color(0xFFE53935)

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
        product.name.take(2).uppercase()
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .graphicsLayer {
                scaleX = finalScale
                scaleY = finalScale
            }
            .shadow(
                elevation = if (isSelected) 12.dp else 4.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = if (isSelected) 0.10f else 0.05f),
                spotColor = Color.Black.copy(alpha = if (isSelected) 0.16f else 0.08f)
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
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSelected) SelectedSurface else Surface)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(HeaderBg),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) TextPrimary
                                else Color(0xFFF1F5F9)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                                .size(24.dp)
                                .graphicsLayer {
                                    scaleX = badgeScale
                                    scaleY = badgeScale
                                }
                                .background(
                                    ButtonSucessColor,
                                    RoundedCornerShape(8.dp)
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

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "$ ${product.price}",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
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

            this@Card.AnimatedVisibility(
                visible = isSelected,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
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
                        .background(DangerSoft)
                        .border(
                            width = 1.dp,
                            color = Danger.copy(alpha = 0.10f),
                            shape = CircleShape
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        ) {
                            removePressed = true
                            repeat(quantity) {
                                removeProduct()
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