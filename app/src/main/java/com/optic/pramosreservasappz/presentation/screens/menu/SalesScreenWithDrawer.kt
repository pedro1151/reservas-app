package com.optic.pramosreservasappz.presentation.screens.menu


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewMode
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.Green

@Composable
fun SalesScreenWithDrawer(
    navController: NavHostController,
    content: @Composable (onMenuClick: () -> Unit) -> Unit
) {

    var isOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 TU PANTALLA
        content(
            onMenuClick = { isOpen = true }
        )

        // 🔥 OVERLAY (click afuera)
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable { isOpen = false }
            )
        }

        // 🔥 DRAWER
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(250)
            ) + fadeOut()
        ) {

            PrincipalMenuDrawer(
                navController = navController,
                onDrawerClose = { isOpen = false }
            )
        }
    }
}