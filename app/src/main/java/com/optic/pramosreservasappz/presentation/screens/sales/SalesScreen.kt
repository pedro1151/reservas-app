package com.optic.pramosreservasappz.presentation.screens.sales

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.sales.Components.SalesContent
import com.optic.pramosreservasappz.presentation.screens.sales.menu.PrincipalMenuDrawer
import com.optic.pramosreservasappz.presentation.screens.sales.topbar.SaleTopBar
import kotlinx.coroutines.launch


@Composable
fun SalesScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: SalesViewModel = hiltViewModel()

    // 🔹 Usar rememberSaveable para que Compose recuerde el estado del drawer
    // 🔹 Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // Estado de la tab activa
    var selectedTab by remember { mutableStateOf(0) }

    // 🔥 OBSERVAR ESTADO REAL
    val salesState by viewModel.salesState.collectAsState()

    // 🔥 CARGA INICIAL
    LaunchedEffect(Unit) {
        viewModel.loadSales(ownerId = 1)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PrincipalMenuDrawer(
                onDrawerClose = { scope.launch { drawerState.close() } },
                navController = navController
            )
        }
    ) {

        Scaffold(
            topBar = {
                SaleTopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    },
                )
            },
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
                    SalesContent(
                        paddingValues = paddingValues,
                        navController = navController,
                        sales = state.data
                    )
                }
                else -> {}
            }
        }
    }
}