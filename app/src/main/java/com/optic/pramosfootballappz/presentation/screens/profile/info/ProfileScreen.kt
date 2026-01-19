package com.optic.pramosfootballappz.presentation.screens.profile.info

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.presentation.screens.profile.info.components.ProfileContent

@Composable
fun ProfileScreen(navController: NavHostController) {

    Scaffold(
    ) {paddingValues ->
        ProfileContent(paddingValues = paddingValues, navController = navController)
    }

}