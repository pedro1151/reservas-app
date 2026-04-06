package com.optic.pramosreservasappz.presentation.screens.sales.header

import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

@Composable
fun SaleHeader(
    todayTotal:     Double,
    todayCount:     Int,
    yesterdayTotal: Double,
    monthTotal:     Double,
    balanceHidden:  Boolean,
    onToggleHide:   () -> Unit,
    navController: NavHostController,
    listState: LazyListState
) {

    val collapseFraction by remember {
        derivedStateOf {
            val offset = listState.firstVisibleItemScrollOffset
            val index = listState.firstVisibleItemIndex

            if (index > 0) 1f
            else (offset / 300f).coerceIn(0f, 1f)
        }
    }

    val headerHeight by animateDpAsState(
        targetValue = androidx.compose.ui.unit.lerp(260.dp, 110.dp, collapseFraction),
        label = "headerHeight"
    )

    val isCollapsed = collapseFraction > 0.6f

    AnimatedContent(
        targetState = isCollapsed,
        transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
        label = "headerContent"
    ) { collapsed ->

        if (!collapsed) {
            SaleFullHeader(
                todayTotal = todayTotal,
                todayCount = todayCount,
                yesterdayTotal = yesterdayTotal,
                monthTotal = monthTotal,
                balanceHidden = balanceHidden,
                onToggleHide = onToggleHide,
                navController = navController,
                listState = listState
            )
        } else {
            SaleMiniHeader(
                todayTotal = todayTotal,
                balanceHidden = balanceHidden,
                onToggleHide = onToggleHide,
                navController = navController
            )
        }
    }
}


