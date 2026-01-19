package com.optic.pramosfootballappz.presentation.screens.profile.update

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.presentation.components.DefaultTopBar
import com.optic.pramosfootballappz.presentation.screens.profile.update.components.ProfileUpdateContent
import com.optic.pramosfootballappz.presentation.screens.profile.update.components.UpdateUser

@Composable
fun ProfileUpdateScreen(navController: NavHostController, userParam: String) {

    Log.d("ProfileUpdateScreen", "Data: $userParam")

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = "Actualizar perfil",
                navController = navController,
                upAvailable = true
            )
        }
    ) {paddingValues ->
        ProfileUpdateContent(paddingValues = paddingValues)
    }
    UpdateUser()

}