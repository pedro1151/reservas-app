package com.optic.pramosfootballappz.presentation.screens.roles

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.presentation.components.DefaultTopBar
import com.optic.pramosfootballappz.presentation.screens.roles.components.RolesContent

@Composable
fun RolesScreen(navController: NavHostController) {

    Scaffold(
        topBar = { DefaultTopBar(title = "Selecciona un rol") }
    ) { paddingValues ->
        RolesContent(paddingValues, navController)
    }

}