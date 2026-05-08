package com.optic.pramozventicoappz.presentation.screens.newsale.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun MiniCart(
    total: Double = 0.0,
    totalItems: Int = 0,
    onPositioned: (Offset) -> Unit,
    modifier: Modifier,

) {

    val animatedTotal = remember { androidx.compose.animation.core.Animatable(total.toFloat()) }

    LaunchedEffect(total) {
        animatedTotal.animateTo(
            targetValue = total.toFloat(),
            animationSpec = tween(400)
        )
    }

    val scale = remember { androidx.compose.animation.core.Animatable(1f) }

    LaunchedEffect(total) {
        scale.snapTo(1f)
        scale.animateTo(1.08f, tween(120))
        scale.animateTo(1f, tween(120))
    }



    Row(
        modifier = modifier
            .wrapContentWidth()
        .onGloballyPositioned { coords ->
        val pos = coords.localToRoot(Offset.Zero)
        onPositioned(pos) // 🔥 ENVÍA AL PADRE
    },
        horizontalArrangement = Arrangement.End
    ) {

        Row(
            modifier = Modifier
                .animateContentSize() // 🔥 SUAVIZA CAMBIO DE TAMAÑO
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(8.dp))

            // 💰 TOTAL ANIMADO
            Text(
                text = "$ ${"%.2f".format(animatedTotal.value)}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            Spacer(Modifier.width(10.dp))

            // 📦 ITEMS (también con mini animación)
            AnimatedContent(
                targetState = totalItems,
                transitionSpec = {
                    fadeIn(tween(150)) + slideInVertically { it / 2 } togetherWith
                            fadeOut(tween(150))
                }
            ) { count ->
                Text(
                    text = "$count items",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }

    Spacer(modifier.height(12.dp))
}