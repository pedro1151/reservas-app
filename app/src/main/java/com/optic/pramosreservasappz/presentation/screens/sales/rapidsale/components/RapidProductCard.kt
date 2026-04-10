package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonGreen
import com.optic.pramosreservasappz.presentation.ui.theme.Grafito
import com.optic.pramosreservasappz.presentation.ui.theme.GreenGradient
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
import com.optic.pramosreservasappz.presentation.ui.theme.GrisSuave
import com.optic.pramosreservasappz.presentation.ui.theme.terciary

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

    // 🔥 posición para animación fly
    var position by remember { mutableStateOf(Offset.Zero) }

    // 🔥 animación SOLO horizontal (sin afectar altura)
    val offsetX by animateDpAsState(
        targetValue = if (quantity > 0) 10.dp else 0.dp,
        animationSpec = tween(190)
    )

    val badgeScale by animateFloatAsState(
        targetValue = if (quantity > 0) 1f else 0f,
        animationSpec = tween(250)
    )

    // 🔤 nombre abreviado
    val shortName = remember(product.name) {
        product.name.take(6).uppercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp) // 🔥 altura FIJA (evita saltos)
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
            containerColor = Color(0xFFF4F5F7) // gris moderno tipo Kyte
        ),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(
            width = 0.6.dp,
            color = Color(0xFFE5E7EB) // 🔥 borde inferior sutil
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔥 BLOQUE IZQUIERDO (caja + nombre)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .offset(x = offsetX), // 🔥 SOLO se mueve horizontal
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🟪 CAJA ABREVIADA
                Box(
                    modifier = Modifier
                        .size(60.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                color = Grafito,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = shortName,
                            color = GrisSuave,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 🔥 BADGE (cantidad)
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
                                    color = ButtonGreen.copy(0.95F),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.width(12.dp))

                // 📝 NOMBRE COMPLETO
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF111827)
                )
            }

            // 💰 PRECIO (FIJO a la derecha)
            Text(
                text = "$ ${product.price}",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )

            // ❌ BOTÓN BORRAR TODO
            if (quantity > 0) {
                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = Color(0xFFF87171).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            repeat(quantity) { removeProduct() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "×",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
