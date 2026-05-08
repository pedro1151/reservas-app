package com.optic.pramozventicoappz.presentation.screens.recibos.footer

import android.view.View
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.doOnPreDraw
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun LoadingBox(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

suspend fun View.awaitPreDrawReady() {
    suspendCancellableCoroutine<Unit> { continuation ->

        if (width > 0 && height > 0) {
            doOnPreDraw {
                if (continuation.isActive) {
                    continuation.resume(Unit)
                }
            }
        } else {
            post {
                doOnPreDraw {
                    if (continuation.isActive) {
                        continuation.resume(Unit)
                    }
                }
            }
        }
    }
}

/* ------------------------------------------------ */
/* FOOTER                                           */
/* ------------------------------------------------ */

@Composable
fun ReceiptFooter(
    onWhatsappClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onNewSaleClick: () -> Unit
) {

    val primary = MaterialTheme.colorScheme.primary
    val primarySoft = MaterialTheme.colorScheme.primaryContainer
    val surface = Color.White

    Surface(
        shadowElevation = 18.dp,
        tonalElevation = 0.dp,
        color = surface,
        shape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White,
                            primarySoft.copy(alpha = 0.55f)
                        )
                    )
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 14.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ReciboFooterButton(
                    modifier = Modifier.weight(1f),
                    title = "WhatsApp",
                    icon = Icons.Outlined.Share,
                    onClick = onWhatsappClick,
                    primary = primary,
                    background = Color.White,
                    selectedBackground = primarySoft
                )

                ReciboFooterButton(
                    modifier = Modifier.weight(1f),
                    title = "Descargar",
                    icon = Icons.Outlined.Download,
                    onClick = onDownloadClick,
                    primary = primary,
                    background = Color.White,
                    selectedBackground = primarySoft
                )

                ReciboFooterButton(
                    modifier = Modifier.weight(1f),
                    title = "Nueva Venta",
                    icon = Icons.Outlined.AddShoppingCart,
                    onClick = onNewSaleClick,
                    primary = primary,
                    background = primary,
                    selectedBackground = primary
                )
            }
        }
    }
}

@Composable
fun ReciboFooterButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    primary: Color,
    background: Color,
    selectedBackground: Color
) {

    val isPrimaryButton = background == primary

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(120),
        label = "footer_button_scale"
    )

    val containerColor =
        if (isPrimaryButton) {
            primary
        } else {
            background
        }

    val contentColor =
        if (isPrimaryButton) {
            Color.White
        } else {
            primary
        }

    val iconBackground =
        if (isPrimaryButton) {
            Color.White.copy(alpha = 0.18f)
        } else {
            selectedBackground.copy(alpha = 0.95f)
        }

    OutlinedButton(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .height(64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPrimaryButton) 10.dp else 5.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = primary.copy(alpha = if (isPrimaryButton) 0.20f else 0.08f),
                spotColor = primary.copy(alpha = if (isPrimaryButton) 0.24f else 0.10f)
            ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isPrimaryButton) 0.dp else 1.1.dp,
            color = if (isPrimaryButton) Color.Transparent else primary.copy(alpha = 0.18f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 8.dp
        )
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                maxLines = 1,
                color = contentColor
            )
        }
    }
}