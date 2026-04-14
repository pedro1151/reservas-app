package com.optic.pramosreservasappz.presentation.screens.rapidsale


import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
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
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.MiniCart
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.NewRapidProduct
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.ProductMiniCard
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.RapidProductCard
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.RapidSaleSearchBar
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Cyan
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonGreen
import com.optic.pramosreservasappz.presentation.ui.theme.GreenGradient
import com.optic.pramosreservasappz.presentation.ui.theme.GrisSuave
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import com.optic.pramosreservasappz.presentation.util.getCurrentFormattedDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RapidSaleContent(
    products: List<ProductResponse>,
    navController: NavHostController,
    viewModel: SalesViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier
) {
    // posicion del carrito
    var cartPosition by remember { mutableStateOf(Offset.Zero) }

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

    LaunchedEffect(hasQuery, filteredProducts) {
        showCreateAnimated = false
        if (hasQuery && filteredProducts.isEmpty()) {
            kotlinx.coroutines.delay(1000)
            showCreateAnimated = true
        }
    }

        Column(
            modifier
                .fillMaxWidth()
                .background(SoftCoolBackground)
        ) {

            // 🟢 HEADER (DIVISIÓN VISUAL)
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
                    modifier =  Modifier.fillMaxWidth()
                )
            }

            // ⚪ CONTENIDO BLANCO
            Column(
                modifier =  Modifier
                    .fillMaxWidth()
                    .background(SoftCoolBackground)
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

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f) // 🔥 CLAVE
                                .padding(horizontal = 4.dp, vertical = 1.dp),
                            contentPadding = PaddingValues(bottom = 2.dp)
                        ) {
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
                                            addProduct = { viewModel.addProduct(product) },
                                            removeProduct = { viewModel.removeProduct(product) },
                                            modifier = Modifier.animateItemPlacement(),
                                            viewModel = viewModel
                                        )
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
                                    navController.navigate(ClientScreen.RapidSaleResumen.route)
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
                                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                contentDescription = "Continuar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

    // 🔥 OVERLAY ENCIMA CUANDO NO HAY PRODUCTOS EXISTENTES
    if (hasQuery && filteredProducts.isEmpty()) {

        AnimatedVisibility(
            visible = showCreateAnimated,
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
                    .clickable { /* opcional: cerrar */ },
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
                                    userId = 1,
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
                                // 🔥 cerrar popup
                                showCreateAnimated = false

                                // 🔥 limpiar búsqueda
                                viewModel.onSearchQueryChanged("")

                                // 🔥 limpiar inputs
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



    // ANIMACION DE PRODUCTO QUE VUELA AL CARRITO AL AGREGARLO
    val flyData by viewModel.flyAnimation.collectAsState()
  /*  NO SE USA DE MOMENTO
    flyData?.let { data ->

        val animX = remember { Animatable(data.startX) }
        val animY = remember { Animatable(data.startY) }
        val scale = remember { Animatable(1f) }

        LaunchedEffect(data) {

            launch {
                animX.animateTo(
                    targetValue = cartPosition.x,
                    animationSpec = tween(600)
                )
            }

            launch {
                animY.animateTo(
                    targetValue = cartPosition.y,
                    animationSpec = tween(600)
                )
            }

            launch {
                scale.animateTo(
                    0.3f,
                    animationSpec = tween(600)
                )
            }

            delay(600)
            viewModel.clearFlyAnimation()
        }

        Box(
            modifier =modifier
                .fillMaxSize()
        ) {

            // 🔥 ELEMENTO VOLANDO
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            animX.value.toInt(),
                            animY.value.toInt()
                        )
                    }
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        alpha = 0.9f
                    }
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                    .padding(8.dp)
            ) {

                //data.product.name,
                  //  "$ ${data.product.price}",
                ProductMiniCard(
                    name = data.product.name,
                    price = "$ ${data.product.price}",
                    modifier =  Modifier
                )
            }
        }
    }

   */

}