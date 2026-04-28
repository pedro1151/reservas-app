package com.optic.pramosreservasappz.presentation.screens.newsale.steptwo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.components.PullRefreshWrapper
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel

@Composable
fun CompleteSaleStepTwoScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: NewSaleViewModel
) {

    // posicion del carrito
    var cartPosition by remember { mutableStateOf(Offset.Zero) }

    val productsResource by viewModel.productsState.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts(ownerId = 1, name = "")
    }

    LaunchedEffect( productsResource ) {
        if ( productsResource  !is Resource.Loading) isRefreshing = false
    }
    // BORRO PRODUCTOS SELECCIONADOS VIEJOS DEL CARRITO
    /* LaunchedEffect(Unit) {
         viewModel.clearSelectedProducts()
     }

     */

    val total by viewModel.total.collectAsState()
    val totalItems by viewModel.totalItems.collectAsState()

    val backRoute =
        when (viewModel.saleFlowType) {
            SaleType.RAPID -> ClientScreen.Sales.route
            SaleType.COMPLETE -> ClientScreen.CompleteSaleStepOne.route
            null -> null
        }

    Scaffold(
        topBar = {


            BackTopBar(
                title = "Seleccionar Items",
                navController = navController,
                back = backRoute
            )
        }
    ) { paddingValues ->

        PullRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.loadProducts(ownerId = 1, name = "")
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = productsResource) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                is Resource.Success -> {
                    CompleteSaleStepTwoContent(
                        products = result.data,
                        viewModel = viewModel,
                        navController = navController,
                        paddingValues = paddingValues,
                        modifier = Modifier
                    )
                }
                is Resource.Failure -> {
                    ErrorState(onRetry = { isRefreshing = true; viewModel.loadProducts(ownerId = 1, name = "") })
                }
                else -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Color.Black, strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color(0xFFF5F5F5)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.WifiOff, null, tint = Color(0xFFCCCCCC), modifier = Modifier.size(28.dp))
                }
            }
            Text("Sin conexión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text("Jala hacia abajo para reintentar", fontSize = 13.sp, color = Color(0xFFAAAAAA))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A)),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text("Reintentar", fontSize = 13.sp, color = Color.White)
            }
        }
    }
}
