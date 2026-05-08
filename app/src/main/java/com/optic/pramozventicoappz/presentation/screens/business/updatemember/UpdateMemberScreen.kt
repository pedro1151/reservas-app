package com.optic.pramozventicoappz.presentation.screens.business.updatemember


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.screens.business.BusinessViewModel

@Composable
fun UpdateMemberScreen(
    navController: NavHostController,
    userId: Int
){

    val viewModel        : BusinessViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Actualizar Colaborador"
            )
        },

        containerColor = Color.White
    ) { paddingValues ->


        UpdateMemberContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navController = navController,
            userId = userId
        )


    }
}

