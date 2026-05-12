package com.optic.pramozventicoappz.presentation.screens.recibos.configuracion

import com.optic.pramozventicoappz.presentation.screens.inicio.SalesViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.errorstate.DefaultErrorState
import com.optic.pramozventicoappz.presentation.components.loading.DefaultLoadingState
import com.optic.pramozventicoappz.presentation.sales.SalesContent
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel


@Composable
fun RecibosConfigScreen(
    navController: NavHostController
) {



    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Text("Rellenar aqui pantalla de configuracion de recibo ")
    }

}