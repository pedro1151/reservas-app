package com.optic.pramosreservasappz.presentation.screens.rapidsale.resumen

import androidx.activity.ComponentActivity
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*

import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleItemCreateWithoutSaleId

import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonGreen
import com.optic.pramosreservasappz.presentation.util.getCurrentFormattedDate
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PullRefreshWrapper
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel

@Composable
fun RapidSaleResumenScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: SalesViewModel
) {

    val total by viewModel.total.collectAsState()
    val totalItems by viewModel.totalItems.collectAsState()
    val selectedProducts by viewModel.selectedProducts.collectAsState()

    val createWithItemsSaleState by viewModel.createSaleWithItemsState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreating by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        println("Items: ${selectedProducts.size}")
    }


    // ESCUCHAR EL ESTADO DE LA CREACION DE LA VENTA + ITEMS
    LaunchedEffect( createWithItemsSaleState ) {
        when (val state = createWithItemsSaleState) {

            is Resource.Success -> {

                viewModel.clearSelectedProducts()
                // reseteo el estado, sino va a redireccionar mal
                viewModel.resetCreateSaleWithItemsState()
                navController.navigate(ClientScreen.Sales.route) {
                    popUpTo(ClientScreen.RapidSaleResumen.route) { inclusive = true }
                }

                isCreating = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error: ${state.message}", duration = SnackbarDuration.Long)
                }
                isCreating = false
            }

            is Resource.Loading -> {
                isCreating = true
            }
            else -> {
                isCreating = false
            }
        }
    }


    Scaffold(
        topBar = {
            BackTopBar(
                title = "VOLVER AL CARRITO",
                navController = navController
            )
        },
        bottomBar = {
            // 🔥 FOOTER FIJO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // BOTON DE CREAR VENTA RAPIDA
                Button(
                    onClick = {

                        val generatedName = "Venta ${getCurrentFormattedDate()}"

                        val items = selectedProducts.map {
                            SaleItemCreateWithoutSaleId(
                                productId = it.first.id,
                                quantity = it.second,
                                price = it.first.price
                            )
                        }

                        val totalAmount = selectedProducts.sumOf {
                            it.first.price * it.second
                        }

                        val saleRequest = CreateSaleWithItemsRequest(
                            sale = SaleCreateRequest(
                                ownerId = 1,
                                createdByUserId = 1,
                                amount = totalAmount,
                                description = generatedName
                            ),
                            items = items
                        )

                        viewModel.createSaleWithItems(saleRequest)

                    },
                    enabled = !isCreating,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = SGray400
                    )
                ) {
                    Text("CREAR VENTA", color = Color.White)
                }
            }
        }
    ) { paddingValues ->

        // LISTA DE RESUMEN DE PRODUCTOS
        RapidSaleResumenContent(
            selectedProducts =  selectedProducts,
            paddingValues  = paddingValues,
            total = total,
            totalItems = totalItems
        )
    }

    SnackbarHost(
        hostState = snackbarHostState,
        //   modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
    ) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = Color(0xFF1A1A1A),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }

    AnimatedVisibility(
        visible = isCreating,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .fillMaxSize() // 🔥 clave
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f)), // 🔥 blur fake moderno
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


