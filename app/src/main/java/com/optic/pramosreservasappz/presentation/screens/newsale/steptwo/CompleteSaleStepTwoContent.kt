package com.optic.pramosreservasappz.presentation.screens.newsale.steptwo


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductViewType
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.components.MiniCart
import com.optic.pramosreservasappz.presentation.screens.newsale.components.NewRapidProduct
import com.optic.pramosreservasappz.presentation.screens.newsale.components.ProductNotFoundAction
import com.optic.pramosreservasappz.presentation.screens.newsale.components.RapidProductCard
import com.optic.pramosreservasappz.presentation.screens.newsale.components.RapidProductGridCard
import com.optic.pramosreservasappz.presentation.screens.newsale.components.RapidSaleSearchToolbar
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompleteSaleStepTwoContent(
    products: List<MiniProductResponse>,
    navController: NavHostController,
    viewModel: NewSaleViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier,
    businessId: Int
) {
    // posicion del carrito
    var cartPosition by remember { mutableStateOf(Offset.Zero) }

    val background = MaterialTheme.colorScheme.background

    val createWithItemsSaleState by viewModel.createSaleWithItemsState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isCreating by remember { mutableStateOf(false) }



    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val query by viewModel.searchQuery.collectAsState()
    val localProducts by viewModel.localProductsList.collectAsState()

    var productName by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productsState by viewModel.productsState.collectAsState()
    val selectedProducts by viewModel.selectedProducts.collectAsState()

    //ESCUCHAR EL ESTADO DE LA CREACION DE UN PRODUCTO
    val createProductState by viewModel.createProductState
    LaunchedEffect(createProductState) {
        when (val state = createProductState) {
            is Resource.Success -> {

                val newProduct = state.data

                val qty = cantidad.toIntOrNull() ?: 1

                repeat(qty) {
                    viewModel.addProduct(newProduct)
                }

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


    // 🔥 VALIDACIÓN
    val isEnabled = productName.isNotBlank() &&
            priceText.isNotBlank() &&
            cantidad.isNotBlank()



    // para busqueda
    val hasQuery = query.isNotBlank()
    val filteredProducts = remember(query, localProducts) {
        if (query.isBlank()) localProducts
        else localProducts.filter { service ->
            service.name.contains(query, ignoreCase = true) == true
        }
    }

    val total by viewModel.total.collectAsState()
    val totalItems by viewModel.totalItems.collectAsState()

    val canConfirm =  selectedProducts.isNotEmpty()

    // animacion de card de new prodict
    var showCreateAnimated by remember { mutableStateOf(false) }

// dentro de RapidSaleContent()

    val viewType by viewModel.productViewType.collectAsState()
    var showCreateSheet by remember { mutableStateOf(false) }
    /*
    LaunchedEffect(hasQuery, filteredProducts) {
        showCreateAnimated = false
        if (hasQuery && filteredProducts.isEmpty()) {
            kotlinx.coroutines.delay(1000)
            showCreateAnimated = true
        }
    }

     */

    Column(
        modifier
            .fillMaxWidth()
            .background(background)
    ) {

        // 🟢 HEADER (DIVISIÓN VISUAL)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {

            RapidSaleSearchToolbar(
                query            = query,
                onQueryChange    = { viewModel.onSearchQueryChanged(it) },
                hasQuery         = hasQuery,
                totalCount       = localProducts.size,
                filteredCount    = filteredProducts.size,
                viewType         = viewType,
                onViewTypeChange = { viewModel.updateProductViewType( it) },
                onAddClick = {
                    showCreateSheet = true   // 🔥 SOLO ABRIR
                }
            )
        }

        // ⚪ CONTENIDO BLANCO
        Column(
            modifier =  Modifier
                .fillMaxWidth()
                .background(background)
        ) {

            when (productsState) {

                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                is Resource.Failure -> {
                    Text(
                        "Error cargando productos",
                        color = SRed,
                        modifier = Modifier.padding(16.dp)
                    )
                }


                is Resource.Success -> {

                    if (hasQuery && filteredProducts.isEmpty()) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(background),
                            contentAlignment = Alignment.Center
                        ) {
                            ProductNotFoundAction(
                                query = query,
                                onAddClick = {
                                    productName = query
                                    showCreateSheet = true
                                }
                            )
                        }

                    } else {

                        when (viewType) {

                            // ─────────────────────────────────────────────
                            // LIST MODE
                            // ─────────────────────────────────────────────
                            ProductViewType.LIST -> {

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 8.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    items(
                                        items = filteredProducts,
                                        key = { it.id }
                                    ) { product ->

                                        val inCart =
                                            selectedProducts.find { it.first.id == product.id }

                                        var visible by remember { mutableStateOf(false) }

                                        LaunchedEffect(Unit) {
                                            visible = true
                                        }

                                        AnimatedVisibility(
                                            visible = visible,
                                            enter = fadeIn(tween(220)) +
                                                    slideInVertically(
                                                        animationSpec = tween(220)
                                                    ) { it / 4 }
                                        ) {

                                            RapidProductCard(
                                                product = product,
                                                inCart = inCart,
                                                addProduct = { viewModel.addProduct(product) },
                                                removeProduct = { viewModel.removeProduct(product) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .animateItemPlacement(
                                                        spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = Spring.StiffnessLow
                                                        )
                                                    ),
                                                viewModel = viewModel
                                            )
                                        }
                                    }
                                }
                            }

                            // ─────────────────────────────────────────────
                            // GRID MODE (2 por fila REAL)
                            // ─────────────────────────────────────────────
                            ProductViewType.GRID -> {

                                val rows = filteredProducts.chunked(3)

                                LazyColumn(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(background),
                                    contentPadding = PaddingValues(
                                        start = 5.dp,
                                        end = 5.dp,
                                        bottom = 5.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    items(
                                        items = rows,
                                        key = { row -> row.first().id }
                                    ) { pair ->

                                        var visible by remember { mutableStateOf(false) }

                                        LaunchedEffect(Unit) {
                                            visible = true
                                        }

                                        AnimatedVisibility(
                                            visible = visible,
                                            enter = fadeIn(tween(220)) +
                                                    slideInVertically(
                                                        animationSpec = tween(220)
                                                    ) { it / 5 }
                                        ) {

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {

                                                pair.forEach { product ->

                                                    val inCart =
                                                        selectedProducts.find {
                                                            it.first.id == product.id
                                                        }

                                                    RapidProductGridCard(
                                                        product = product,
                                                        inCart = inCart,
                                                        addProduct = {
                                                            viewModel.addProduct(product)
                                                        },
                                                        removeProduct = {
                                                            viewModel.removeProduct(product)
                                                        },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .animateItemPlacement(
                                                                spring(
                                                                    dampingRatio =
                                                                    Spring.DampingRatioMediumBouncy,
                                                                    stiffness =
                                                                    Spring.StiffnessLow
                                                                )
                                                            ),
                                                        viewModel = viewModel
                                                    )
                                                }

                                                // si queda impar
                                                if (pair.size == 1) {
                                                    Spacer(
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                            }
                                        }
                                    }
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
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {

                val backgroundColor = if (canConfirm)
                    MaterialTheme.colorScheme.primary
                else
                    SGray400

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // 🔥 centrado real
                ) {

                    Row(
                        modifier = Modifier
                            .height(58.dp)
                            .background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(18.dp) // 🔥 pill moderno
                            )
                            .clickable(enabled = canConfirm) {
                                navController.navigate(ClientScreen.CompleteSaleStepTree.route)
                                viewModel.resetCreateSaleWithItemsState()
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // 🛒 MINI CART (integrado)
                        MiniCart(
                            total = total,
                            totalItems = totalItems,
                            onPositioned = { pos ->
                                cartPosition = pos
                            },
                            modifier = Modifier
                        )

                        Spacer(Modifier.width(12.dp))

                        // 🔥 divisor sutil (tipo Kyte)
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(22.dp)
                                .background(Color.White.copy(alpha = 0.3f))
                        )

                        Spacer(Modifier.width(12.dp))

                        // 👉 ICONO
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continuar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    val shouldShowCreateOverlay = showCreateSheet
       // (hasQuery && filteredProducts.isEmpty()) || showCreateSheet

    if (shouldShowCreateOverlay) {

        AnimatedVisibility(
            visible = shouldShowCreateOverlay,
            enter = fadeIn(tween(200)) +
                    scaleIn(
                        initialScale = 0.85f,
                        animationSpec = spring(
                            dampingRatio = 0.5f,   // 🔥 rebote
                            stiffness = 300f       // 🔥 velocidad
                        )
                    ),
            exit = fadeOut()
        ) {

            val scaleAnim = remember { Animatable(0.85f) }

            LaunchedEffect(Unit) {

                // ANIMACION PARA Q EL POP APAREZCA ON UN REBOTE MODERNO
                scaleAnim.animateTo(
                    1f,
                    animationSpec = spring(
                        dampingRatio = 0.4f,
                        stiffness = 250f
                    )
                )
            }

            // 🔥 FONDO OSCURECIDO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {  },
                contentAlignment = Alignment.Center
            ) {

                // 🔥 CARD
                Box(
                    modifier =  Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .graphicsLayer {
                            scaleX = scaleAnim.value
                            scaleY = scaleAnim.value
                        }
                ) {

                    NewRapidProduct(
                        isEnabled = isEnabled,
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
                                    businessId = businessId,
                                    name = productName,
                                    price = price
                                )
                            )
                        }
                    )

                    // ❌ BOTÓN CERRAR (TOP RIGHT)
                    Box(
                        modifier =  Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                Color.Black.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {

                                showCreateSheet = false
                                showCreateAnimated = false

                                viewModel.onSearchQueryChanged("")

                                productName = ""
                                priceText = ""
                                cantidad = ""
                            }
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
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
            modifier =  Modifier.size(28.dp)
        )
    }


}