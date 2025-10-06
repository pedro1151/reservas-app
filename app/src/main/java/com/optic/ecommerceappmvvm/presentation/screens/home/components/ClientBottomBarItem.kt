package com.optic.ecommerceappmvvm.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenLima

@Composable
fun RowScope.ClientBottomBarItem(
    screen: ClientScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val selectedColor =  MaterialTheme.colorScheme.getGreenLima //   Color(0xFFFF4D4D) // rojo fluorescente suave (inspirado en FotMob)
    val unselectedColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)

    NavigationBarItem(
        label = {
            Text(
                text = screen.title,
                color = if (selected) selectedColor else unselectedColor
            )
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = null,
                tint = if (selected) selectedColor else unselectedColor
            )
        },
        selected = selected,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .weight(1f),
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent // sin el efecto gris del fondo
        ),
        onClick = {
            navController.popBackStack(Graph.CLIENT, inclusive = false)
            navController.navigate(screen.route) {
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}
