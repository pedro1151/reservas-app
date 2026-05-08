package com.optic.pramozventicoappz.presentation.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.sales.SalesContent
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel


@Composable
fun SalesScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    newSaleViewModel: NewSaleViewModel,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode

    val viewModel: SalesViewModel = hiltViewModel()

    // 🔹 Usar rememberSaveable para que Compose recuerde el estado del drawer
    // 🔹 Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()



    // 🔥 OBSERVAR ESTADO REAL
    val salesState by viewModel.salesState.collectAsState()

    // 🔥 CARGA INICIAL
    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadSales(businessId, 25)
        }
    }

        Scaffold(
            containerColor = Color(0xFFF5F5F5)
        ) { paddingValues ->

            when (val state = salesState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                is Resource.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se pudieron cargar las ventas")
                    }
                }
                is Resource.Success -> {
                    SalesScreenWithDrawer(navController) { onMenuClick ->
                        SalesContent(
                            paddingValues = paddingValues,
                            navController = navController,
                            sales = state.data,
                            onMenuClick = onMenuClick,
                            newSaleViewModel = newSaleViewModel
                        )
                    }
                }
                else -> {}
            }
        }

}