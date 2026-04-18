package com.optic.pramosreservasappz.presentation.screens.business.mybusiness

import com.optic.pramosreservasappz.presentation.screens.business.abmmember.ABMBusinessMemberContent

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.screens.business.BusinessViewModel

@Composable
fun MyBusinessScreen(
    navController: NavHostController
){

    val viewModel        : BusinessViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Mi Negocio"
            )
        },

        containerColor = Color.White
    ) { paddingValues ->


        MyBusinessContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navController =  navController
        )


    }
}

