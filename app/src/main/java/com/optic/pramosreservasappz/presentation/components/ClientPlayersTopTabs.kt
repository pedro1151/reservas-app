package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*  // Import Material3
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen

@Composable
fun ClientPlayersTopTabs(
    navController: NavHostController,
    screens: List<ClientScreen>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentIndex = screens.indexOfFirst { it.route == currentDestination?.route }.coerceAtLeast(0)

    // Material3 usa TabRow, no ScrollableTabRow, pero Scrollable es igual con ScrollableTabRow de material3
    ScrollableTabRow(
        selectedTabIndex = currentIndex,
        containerColor = MaterialTheme.colorScheme.primaryContainer,        // color de fondo de la barra
        contentColor = MaterialTheme.colorScheme.primary,        // color del contenido (texto, íconos)
        modifier = Modifier
            .fillMaxWidth()
            .height( 30.dp )  // usar 30.dp,  pero kotlin no permite variables sin declararlas, reemplazar por 30.dp
    ) {
        screens.forEachIndexed { index, screen ->
            Tab(
                selected = index == currentIndex,
                onClick = {
                    if (screen.route != currentDestination?.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                text = {
                    Text(
                        text = screen.title,
                        fontSize = 14.sp,
                        fontWeight = if (index == currentIndex) FontWeight.Bold else FontWeight.Normal,
                        color = if (index == currentIndex)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
                /* icon = {
                    Icon(imageVector = screen.icon, contentDescription = screen.title)
                } */
            )
        }
    }
}
