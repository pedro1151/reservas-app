package com.optic.pramosreservasappz.presentation.screens.mas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.screens.mas.components.MasContent
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosreservasappz.presentation.ui.theme.GreyLight

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