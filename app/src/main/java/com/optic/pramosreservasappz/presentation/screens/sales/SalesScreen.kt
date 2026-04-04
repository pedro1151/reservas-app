package com.optic.pramosreservasappz.presentation.screens.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.sales.Components.SalesContent
import com.optic.pramosreservasappz.presentation.screens.sales.menu.PrincipalMenuDrawer
import com.optic.pramosreservasappz.presentation.screens.sales.menu.topbar.SaleTopBar
import kotlinx.coroutines.launch


@Composable
fun SalesScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: SalesViewModel = hiltViewModel()

    // cerrar abrir menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    // Estado de la tab activa
    var selectedTab by remember { mutableStateOf(0) }

    // 🔥 OBSERVAR ESTADO REAL
    val salesState by viewModel.salesState.collectAsState()

    // 🔥 CARGA INICIAL
    LaunchedEffect(Unit) {
        viewModel.loadSales(ownerId = 1)
    }

    ModalNavigationDrawer(
        drawerState   = drawerState,
        drawerContent = {
            PrincipalMenuDrawer(
                onDrawerClose    = { scope.launch { drawerState.close() } },
                navController = navController
            )
        }
    ) {

        Scaffold(

            topBar = {
                SaleTopBar(
                    onMenuClick  = { scope.launch { drawerState.open() } },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* TODO */ },
                    containerColor = Color(0xFF0D0D0D),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            containerColor = Color(0xFFF5F5F5)
        ) { paddingValues ->

            when (val state = salesState) {

                // 🔄 LOADING
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // ❌ ERROR
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

                // ✅ SUCCESS
                is Resource.Success -> {
                    SalesContent(
                        paddingValues = paddingValues,
                        viewModel = viewModel,
                        navController = navController,
                        selectedTab = selectedTab,
                        onTabChange = { selectedTab = it },
                        sales = state.data // 🔥 PASAMOS DATA REAL
                    )
                }

                else -> {}
            }
        }
    }
}