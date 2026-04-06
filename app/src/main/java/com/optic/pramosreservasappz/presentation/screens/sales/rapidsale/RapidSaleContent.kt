package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleItemCreateWithoutSaleId
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.sales.Components.SAccent
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.MiniCart
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.NewRapidProduct
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.RapidProductCard
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.RapidSaleSearchBar
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.SaleInputDefault
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import com.optic.pramosreservasappz.presentation.util.getCurrentFormattedDate
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RapidSaleContent(
    products: List<ProductResponse>,
    navController: NavHostController,
    viewModel: SalesViewModel
) {

    val createWithItemsSaleState by viewModel.createSaleWithItemsState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreating by remember { mutableStateOf(false) }


    // ESCUCHAR EL ESTADO DE LA CREACION DE LA VENTA + ITEMS
    LaunchedEffect( createWithItemsSaleState ) {
        when (val state = createWithItemsSaleState) {
            is Resource.Success -> {
                navController.navigate(ClientScreen.Sales.route) {
                    popUpTo(ClientScreen.RapidSale.route) { inclusive = true }
                }
                isCreating = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error: ${state.message}", duration = SnackbarDuration.Long)
                }
                isCreating = false
            }

            is Resource.Loading -> isCreating = true
            else -> isCreating = false
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val query by viewModel.searchQuery.collectAsState()
    val localProducts by viewModel.localProductsList.collectAsState()

    var productName by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productsState by viewModel.productsState.collectAsState()
    val selectedProducts = remember {
        mutableStateListOf<Pair<ProductResponse, Int>>()
    }


    //ESCUCHAR EL ESTADO DE LA CREACION DE UN PRODUCTO
    val createProductState by viewModel.createProductState
    LaunchedEffect(createProductState) {
        when (val state = createProductState) {
            is Resource.Success -> {

                val newProduct = state.data

                val qty = cantidad.toIntOrNull() ?: 1

                selectedProducts.add(newProduct to qty)

                // limpiar inputs
                productName = ""
                priceText = ""
                cantidad = ""

                // 🔥 limpiar búsqueda
                viewModel.onSearchQueryChanged("")

            }

            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error al crear producto")
                }
            }

            else -> {}
        }
    }

    /*-----------------------------------------------*/

    fun addProduct(product: ProductResponse) {
        val index = selectedProducts.indexOfFirst { it.first.id == product.id }
        if (index >= 0) {
            val current = selectedProducts[index]
            selectedProducts[index] = current.copy(second = current.second + 1)
        } else {
            selectedProducts.add(product to 1)
        }
    }

    fun removeProduct(product: ProductResponse) {
        val index = selectedProducts.indexOfFirst { it.first.id == product.id }
        if (index >= 0) {
            val current = selectedProducts[index]
            if (current.second <= 1) {
                selectedProducts.removeAt(index)
            } else {
                selectedProducts[index] = current.copy(second = current.second - 1)
            }
        }
    }



     // para busqueda
    val hasQuery = query.isNotBlank()
    val filteredProducts = remember(query, localProducts) {
        if (query.isBlank()) localProducts
        else localProducts.filter { service ->
            service.name.contains(query, ignoreCase = true) == true
        }
    }

    val total = selectedProducts.sumOf { it.first.price * it.second }
    val totalItems = selectedProducts.size

    val canConfirm =  selectedProducts.isNotEmpty()

    // animacion de card de new prodict
    var showCreateAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(hasQuery, filteredProducts) {
        showCreateAnimated = false
        if (hasQuery && filteredProducts.isEmpty()) {
            kotlinx.coroutines.delay(1000)
            showCreateAnimated = true
        }
    }

        Column(
            Modifier
                .fillMaxWidth()
        ) {

            // 🟢 HEADER VERDE (DIVISIÓN VISUAL)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SoftCoolBackground)
                    .padding(5.dp)
            ) {
                // header titulo + boton cerrar

                // Campo de búsqueda ocupa el espacio disponible
                RapidSaleSearchBar(
                    query = query,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ⚪ CONTENIDO BLANCO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SoftCoolBackground)
            ) {

                when (productsState) {

                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            color = SAccent,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is Resource.Failure -> {
                        Text(
                            "Error cargando productos",
                            color = SRed,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is Resource.Success -> {

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f) // 🔥 CLAVE
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(bottom = 12.dp)
                        ) {

                            if (hasQuery && filteredProducts.isEmpty()) {
                                item {
                                    AnimatedVisibility(
                                        visible = showCreateAnimated,
                                        enter = fadeIn(tween(250)) + scaleIn(initialScale = 0.94f),
                                        exit = fadeOut(tween(150))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 30.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // SUGIERO CREAR EL PRODUCTO Y AGREGARLO AL CARRO
                                            NewRapidProduct(
                                                productName = productName,
                                                onProductNameChange = { productName = it },

                                                priceText = priceText,
                                                onPriceChange = { priceText = it },

                                                cantidad = cantidad,
                                                onCantidadChange = { cantidad = it },

                                                createProduct = {
                                                    val price = priceText.toDoubleOrNull()
                                                        ?: return@NewRapidProduct

                                                    viewModel.createProduct(
                                                        ProductCreateRequest(
                                                            userId = 1,
                                                            name = productName,
                                                            price = price
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            else {
                                // lista de productos
                                items(
                                    items = filteredProducts,
                                    key = { it.id }
                                ) { product ->

                                    val inCart =
                                        selectedProducts.find { it.first.id == product.id }
                                    // card de producto animadamente

                                    var visible by remember { mutableStateOf(false) }

                                    LaunchedEffect(Unit) {
                                        visible = true
                                    }

                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = fadeIn(tween(250)) + slideInVertically { it / 3 }
                                    ) {
                                        RapidProductCard(
                                            product = product,
                                            inCart = inCart,
                                            addProduct = { addProduct(product) },
                                            removeProduct = { removeProduct(product) },
                                            modifier = Modifier.animateItemPlacement()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }

                // 🔥 FOOTER FIJO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {

                    if (selectedProducts.isNotEmpty()) {
                        // MINI CARRITO
                        MiniCart(
                            total = total,
                            totalItems = totalItems
                        )
                    }

                    // 🟢 BOTÓN PRINCIPAL
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
                        enabled = canConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            disabledContainerColor = SGray400
                        )
                    ) {
                        Text("GUARDAR VENTA", color = Color.White)
                    }
                }
            }
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
        enter = fadeIn(), exit = fadeOut(),
        //modifier = Modifier.align(Alignment.Center)
    ) {
        CircularProgressIndicator(
            color = Color.Black, strokeWidth = 2.dp,
            modifier = Modifier.size(28.dp)
        )
    }

}