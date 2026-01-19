package com.optic.pramosfootballappz.presentation.screens.menubottombar.components

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
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.presentation.ui.theme.getGreenLima

@Composable
fun RowScope.ClientBottomBarItem(
    screen: ClientScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    // idioma
    val localizedContext = LocalizedContext.current

    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val selectedColor =  MaterialTheme.colorScheme.getGreenLima //   Color(0xFFFF4D4D) // rojo fluorescente suave (inspirado en FotMob)
    val unselectedColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)

    var title_traducido =""
    if (screen.title == "Matches") { title_traducido = localizedContext.getString(R.string.menu_principal_matches)}
    else if (screen.title == "Ligas")  { title_traducido = localizedContext.getString(R.string.menu_principal_leagues)}
    else if (screen.title == "Prode") { title_traducido = localizedContext.getString(R.string.menu_principal_prode)}
    else if (screen.title == "Menu")  { title_traducido = localizedContext.getString(R.string.menu_principal_mas)}
    else if (screen.title == "Siguiendo")  { title_traducido = localizedContext.getString(R.string.menu_principal_follow)}
  //  else { title_traducido = screen.title}
    NavigationBarItem(
        label = {
            Text(
                text = if (title_traducido == "") screen.title else title_traducido,
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
