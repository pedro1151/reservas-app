package com.optic.pramosreservasappz.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshWrapper(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val pullState = rememberPullToRefreshState()
    val density = LocalDensity.current

    val maxOffsetPx = with(density) { 64.dp.toPx() }

    val contentOffsetPx = when {
        pullState.isRefreshing -> maxOffsetPx
        else -> (pullState.progress * maxOffsetPx).coerceIn(0f, maxOffsetPx)
    }

    val animatedOffset by animateFloatAsState(
        targetValue = contentOffsetPx,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "content_offset"
    )

    val indicatorAlpha = if (pullState.isRefreshing) 1f
    else (pullState.progress * 2f).coerceIn(0f, 1f)

    if (pullState.isRefreshing) {
        LaunchedEffect(Unit) { onRefresh() }
    }

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) pullState.endRefresh()
    }

    Box(
        modifier = modifier.nestedScroll(pullState.nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .alpha(indicatorAlpha),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 6.dp,
                border = BorderStroke(0.5.dp, Color(0xFFF0F0F0))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF1A1A1A),
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = with(density) { animatedOffset.toDp() })
        ) {
            content()
        }
    }
}