package com.optic.pramosfootballappz.presentation.screens.mas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.presentation.components.PrimaryTopBar
import com.optic.pramosfootballappz.presentation.screens.mas.components.MasContent
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.presentation.ui.theme.GreyLight

@Composable
fun MasScreen(
    navController: NavHostController,
    isAuthenticated: Boolean
    ) {

    val localizedContext = LocalizedContext.current

    Scaffold (
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Mas"
            )
        },
        containerColor = GreyLight
    ){ paddingValues ->
        MasContent(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            isAuthenticated = isAuthenticated,
            localizedContext = localizedContext

        )
    }
}