package com.optic.pramosreservasappz.presentation.sales.Components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.Components.SaleResumeList
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleFullHeader
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleHeader
import com.optic.pramosreservasappz.presentation.screens.sales.header.SaleMiniHeader
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import java.time.LocalDate

@Composable
fun SalesContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
    sales: List<SaleResponse> // 🔥 NUEVO
) {
    var showRegisterSale by remember { mutableStateOf(false) }
    var balanceHidden by remember { mutableStateOf(false) }


    val today = LocalDate.now()

    fun parseDate(date: String): LocalDate {
        return LocalDate.parse(date.substring(0, 10))
    }

    val todaySales = sales.filter { parseDate(it.created) == today }
    val todayTotal = todaySales.sumOf { it.amount }
    val todayCount = todaySales.size

    val yesterdayTotal = sales
        .filter { parseDate(it.created) == today.minusDays(1) }
        .sumOf { it.amount }

    val monthTotal = sales
        .filter { parseDate(it.created).month == today.month }
        .sumOf { it.amount }

    val listState = rememberLazyListState()

    val collapseFraction by remember {
        derivedStateOf {
            val offset = listState.firstVisibleItemScrollOffset
            val index = listState.firstVisibleItemIndex

            if (index > 0) 1f
            else (offset / 300f).coerceIn(0f, 1f)
        }
    }

    val isCollapsed = collapseFraction > 0.6f
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(SoftCoolBackground)
    ) {

        SaleFullHeader(
            todayTotal = todayTotal,
            todayCount = todayCount,
            yesterdayTotal = yesterdayTotal,
            monthTotal = monthTotal,
            balanceHidden = balanceHidden,
            onToggleHide = { balanceHidden = !balanceHidden },
            navController = navController,
            listState = listState
        )

        SaleResumeList(
            sales = sales,
            today = today,
            listState = listState,
            modifier = Modifier.weight(1f),
            navController =  navController
        )
    }


}

