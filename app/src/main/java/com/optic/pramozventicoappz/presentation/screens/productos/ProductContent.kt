package com.optic.pramozventicoappz.presentation.screens.productos

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultEmptyState
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultSearchEmptyState
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.productos.components.CatalogHeroStats
import com.optic.pramozventicoappz.presentation.screens.productos.components.PrincipalProducCard
import com.optic.pramozventicoappz.presentation.screens.productos.components.ProductGridCard
import com.optic.pramozventicoappz.presentation.screens.productos.components.ProductSearchRow
import com.optic.pramozventicoappz.presentation.ui.theme.Grafito
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductContent(
    modifier         : Modifier = Modifier,
    products         : List<MiniProductResponse>,
    paddingValues    : PaddingValues,
    viewModel        : ProductViewModel,
    navController    : NavHostController,
    isAuthenticated  : Boolean = false,
    localizedContext : Context
) {
    val query         by viewModel.searchQuery.collectAsState()
    val deleteState   by viewModel.deleteProductState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }

    val viewType by viewModel.productViewType.collectAsState()

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Producto eliminado ✓",
                        duration = SnackbarDuration.Short
                    )
                }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Error: ${state.message}",
                        duration = SnackbarDuration.Long
                    )
                }
                isDeleting = false
            }
            is Resource.Loading -> isDeleting = true
            else                -> isDeleting = false
        }
    }

    val hasQuery         = query.isNotBlank()
    val filteredProducts = remember(query, products) {
        if (query.isBlank()) products
        else products.filter { it.name.contains(query, ignoreCase = true) }
    }

    val totalValue = remember(products) {
        products.sumOf {
            try { it.price.toString().toDouble() } catch (_: Exception) { 0.0 }
        }
    }
    val totalValueText = remember(totalValue) { "Bs. %,.0f".format(totalValue) }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (products.isEmpty() && !hasQuery) {

            DefaultEmptyState(
                icon = Icons.Outlined.Inventory2,
                title = "Sin productos aún",
                message = "Registra tu primer producto para comenzar a ver el catálogo aquí.",
                buttonText = "Agregar producto",
                onAddClick = {
                    navController.navigate(
                        ClientScreen.ABMProduct.createRoute(productId = null, editable = false)
                    )
                }
            )

        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Hero Stats Section ──
                item {
                    AnimatedVisibility(
                        visible = products.isNotEmpty(),
                        enter   = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        CatalogHeroStats(
                            totalProducts = products.size,
                            totalValue    = totalValueText
                        )
                    }
                }

                // ── Search Bar + View Toggle ──
                item {
                    ProductSearchRow(
                        query            = query,
                        onQueryChange    = { viewModel.onSearchQueryChanged(it) },
                        hasQuery         = hasQuery,
                        totalCount       = products.size,
                        filteredCount    = filteredProducts.size,
                        viewType         = viewType,
                        onViewTypeChange = { viewModel.updateProductViewType( it) }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ── Empty search state ──
                if (hasQuery && filteredProducts.isEmpty()) {
                    item {
                        DefaultSearchEmptyState(
                            query = query,
                            label = "items"
                        )
                    }
                } else {
                    when (viewType) {

                        // ─ List mode (with swipe actions) ─
                        ProductViewType.LIST -> {
                            items(items = filteredProducts, key = { it.id }) { product ->
                                PrincipalProducCard(
                                    product       = product,
                                    navController = navController,
                                    onDelete      = { viewModel.deleteProduct(it.id) },
                                    modifier      = Modifier
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        )
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }

                        // ─ Grid mode (catalog browse) ─
                        ProductViewType.GRID -> {
                            val chunkedProducts = filteredProducts.chunked(2)
                            items(items = chunkedProducts, key = { it.first().id }) { pair ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    pair.forEachIndexed { index, product ->   // ← forEachIndexed
                                        ProductGridCard(
                                            product       = product,
                                            modifier      = Modifier.weight(1f),
                                            navController = navController,
                                            onDelete      = { viewModel.deleteProduct(it.id) },
                                            isLeftColumn  = index == 0          // ← 0 = izquierda, 1 = derecha
                                        )
                                    }
                                    if (pair.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }

        // ── Snackbar ──
        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData    = data,
                containerColor  = Grafito,
                contentColor    = Color.White,
                shape           = RoundedCornerShape(14.dp)
            )
        }

        // ── Loading overlay (while deleting) ──
        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(),
            exit     = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 10.dp
            ) {
                Box(Modifier.padding(22.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color       = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}