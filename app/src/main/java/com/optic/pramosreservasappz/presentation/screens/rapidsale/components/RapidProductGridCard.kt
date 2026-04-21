package com.optic.pramosreservasappz.presentation.screens.rapidsale.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
@Composable
fun RapidProductGridCard(
    product: ProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<ProductResponse, Int>?,
    modifier: Modifier = Modifier,
    viewModel: SalesViewModel
) {

    val quantity = inCart?.second ?: 0

    val Cyan = Color(0xFF06B6D4)

    // 🔥 verde moderno elegante (no chillón)
    val SuccessGreen = Color(0xFF16A34A)
    val SuccessGreenSoft = Color(0xFFDCFCE7)

    val Border = Color(0xFFE2E8F0)
    val Text = Color(0xFF0F172A)
    val Sub = Color(0xFF64748B)

    var position by remember { mutableStateOf(Offset.Zero) }

    val scale by animateFloatAsState(
        targetValue = if (quantity > 0) 1.02f else 1f,
        animationSpec = tween(220),
        label = ""
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1f else 0f,
        animationSpec = spring(),
        label = ""
    )

    val initials = remember(product.name) {
        product.name.take(2).uppercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp) // 🔥 más pequeño
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
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
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(1.dp, Border),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ─────────────────────────────
            // HEADER
            // ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        if (quantity > 0)
                            Brush.linearGradient(
                                listOf(
                                    Cyan.copy(alpha = 0.16f),
                                    Cyan.copy(alpha = 0.04f)
                                )
                            )
                        else Brush.linearGradient(
                            listOf(
                                Color(0xFFF8FAFC),
                                Color.White
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (quantity > 0) Cyan
                            else Color(0xFFF1F5F9)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        initials,
                        color = if (quantity > 0) Color.White else Sub,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // 🔥 badge verde elegante
                if (quantity > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(26.dp)
                            .graphicsLayer {
                                scaleX = badgeScale
                                scaleY = badgeScale
                            }
                            .background(
                                SuccessGreen,
                                RoundedCornerShape(9.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            quantity.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // ─────────────────────────────
            // BODY
            // ─────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = product.name,
                    color = Text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "$ ${product.price}",
                    color = Cyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.weight(1f))
                Spacer(Modifier.height(15.dp))
                AnimatedVisibility(
                    visible = quantity > 0,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2))
                            .clickable {
                                repeat(quantity) {
                                    removeProduct()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}