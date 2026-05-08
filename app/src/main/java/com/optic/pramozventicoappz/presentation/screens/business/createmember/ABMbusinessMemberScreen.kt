package com.optic.pramozventicoappz.presentation.screens.business.createmember



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.screens.business.BusinessViewModel

@Composable
fun ABMbusinessMemberScreen(
    navController: NavHostController
){

    val viewModel        : BusinessViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Nuevo miembro"
            )
        },

        containerColor = Color.White
    ) { paddingValues ->


        ABMBusinessMemberContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navController = navController
        )


    }
}

