package com.optic.pramozventicoappz.presentation.screens.newsale.steptree

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleItemCreateWithoutSaleId
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.sales.Components.SGray400
import com.optic.pramozventicoappz.presentation.util.getCurrentFormattedDate
import kotlinx.coroutines.launch
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.optic.pramozventicoappz.domain.model.sales.types.SaleType
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramozventicoappz.presentation.ui.theme.ButtonSucessColor

@Composable
fun CompleteSaleStepTreeScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: NewSaleViewModel,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode

    val total by viewModel.total.collectAsState()
    val totalItems by viewModel.totalItems.collectAsState()
    val selectedProducts by viewModel.selectedProducts.collectAsState()

    val createWithItemsSaleState by viewModel.createSaleWithItemsState


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isCreating by remember { mutableStateOf(false) }
    var saleCreated by remember { mutableStateOf(false) }

    // 🔥 guardar id venta creada
    var createdSaleId by remember { mutableStateOf(0) }

    LaunchedEffect(createWithItemsSaleState) {
        when (val state = createWithItemsSaleState) {

            is Resource.Success -> {
                isCreating = false
                saleCreated = true

                // 🔥 guardar id del SaleResponse
                createdSaleId = state.data.id
            }

            is Resource.Failure -> {
                isCreating = false

                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Error: ${state.message}",
                        duration = SnackbarDuration.Long
                    )
                }
            }

            is Resource.Loading -> {
                isCreating = true
            }

            else -> {
                isCreating = false
            }
        }
    }

    val newSaleRoute =
        when (viewModel.saleFlowType) {
            SaleType.RAPID -> ClientScreen.CompleteSaleStepTwo.route
            SaleType.COMPLETE -> ClientScreen.CompleteSaleStepOne.route
            null -> ClientScreen.CompleteSaleStepTwo.route
        }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Resumen",
                navController = navController,
                onClientClick = {
                    navController.navigate(ClientScreen.SelecClient.route)
                },
                back = ClientScreen.CompleteSaleStepTwo.route
            )
        },

        bottomBar = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){

                AnimatedVisibility(
                    visible = !saleCreated,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Button(
                        onClick = {

                            var generatedName = viewModel.saleName

                            if (generatedName.isNullOrBlank()) {
                                generatedName =
                                    "Venta ${getCurrentFormattedDate()}"
                            }

                            val items = selectedProducts.map {
                                SaleItemCreateWithoutSaleId(
                                    productId = it.first.id,
                                    quantity = it.second,
                                    price = it.first.price
                                )
                            }

                            val totalAmount =
                                selectedProducts.sumOf {
                                    it.first.price * it.second
                                }

                            val saleRequest =
                                CreateSaleWithItemsRequest(
                                    sale = SaleCreateRequest(
                                        businessId = businessId,
                                        createdByUserId = userId,
                                        amount = totalAmount,
                                        description = generatedName,
                                        paymentMethod = viewModel.paymentMethod,
                                        clientId = viewModel.selectedClientId
                                    ),
                                    items = items
                                )

                            viewModel.createSaleWithItems(saleRequest)
                        },
                        enabled = !isCreating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                            MaterialTheme.colorScheme.primary,
                            disabledContainerColor = SGray400
                        )
                    ) {
                        Text(
                            "CREAR VENTA",
                            color = Color.White
                        )
                    }
                }

                AnimatedVisibility(
                    visible = saleCreated,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Column(
                        verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                    ) {

                        // 🔥 Compartir recibo
                        Button(
                            onClick = {
                                navController.navigate(
                                    ClientScreen.ReciboDetail.createRoute(
                                        createdSaleId
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                ButtonSucessColor
                            )
                        ) {
                            Text(
                                "Generar Recibo",
                                color = Color.White
                            )
                        }

                        // Nueva venta
                        Button(
                            onClick = {

                                viewModel.resetCreateSaleWithItemsState()
                                viewModel.clearSelectedProducts()
                                viewModel.resetStepOneState()
                                if (viewModel.isRapidSale()){
                                    viewModel.updateSaleFlowType(SaleType.RAPID)
                                }
                                else{
                                    viewModel.updateSaleFlowType(SaleType.COMPLETE)
                                }
                                navController.navigate(
                                    newSaleRoute  // volver a nueva venta segun el tipo de venta
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                "Nueva venta",
                                color = Color.White
                            )
                        }

                        // Salir
                        TextButton(
                            onClick = {
                                viewModel.clearSelectedProducts()  // limpio carrito
                                viewModel.resetCreateSaleWithItemsState() // limpio estado success de venta

                                navController.navigate(
                                    ClientScreen.Sales.route
                                ) {
                                    popUpTo(
                                        navController.graph
                                            .findStartDestination().id
                                    ) {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Salir",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

    ) { paddingValues ->

        CompleteSaleStepTreeContent(
            selectedProducts = selectedProducts,
            paddingValues = paddingValues,
            total = total,
            totalItems = totalItems,
            viewModel = viewModel,
            navController = navController
        )
    }

    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = Color(0xFF1A1A1A),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }

    // 🔥 spinner garantizado encima de toda la UI
    if (isCreating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}