package com.optic.pramozventicoappz.presentation.screens.productos

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.productos.components.CatalogHeroStats
import com.optic.pramozventicoappz.presentation.screens.productos.components.PrincipalProducCard
import com.optic.pramozventicoappz.presentation.screens.productos.components.ProductGridCard
import com.optic.pramozventicoappz.presentation.screens.productos.components.ProductSearchRow
import kotlinx.coroutines.launch

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue400  = Color(0xFF60A5FA)
private val Blue100  = Color(0xFFDBEAFE)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val Red500   = Color(0xFFEF4444)
private val PageBg   = Color(0xFFF8FAFC)

// ─── Main Content ────────────────────────────────────────────────────────────────
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
            EmptyProductsState {
                navController.navigate(
                    ClientScreen.ABMServicio.createRoute(serviceId = null, editable = false)
                )
            }
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
                    item { CatalogSearchEmptyState(query = query) }
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
                                    pair.forEach { product ->
                                        ProductGridCard (
                                            product       = product,
                                            modifier      = Modifier.weight(1f),
                                            navController = navController,
                                            onDelete      = { viewModel.deleteProduct(it.id) }
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
                containerColor  = Slate900,
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
                        color       = Blue600,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}


// ─── Empty Search State ──────────────────────────────────────────────────────────
@Composable
private fun CatalogSearchEmptyState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Blue50),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.SearchOff, null, tint = Blue600, modifier = Modifier.size(34.dp))
        }
        Text(
            "Sin resultados",
            fontSize      = 17.sp,
            fontWeight    = FontWeight.Bold,
            color         = Slate900,
            letterSpacing = (-0.3).sp
        )
        Text(
            "No hay productos que coincidan con \"$query\"",
            fontSize  = 13.sp,
            color     = Slate400,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}

// ─── Empty Products State ────────────────────────────────────────────────────────
@Composable
private fun EmptyProductsState(onAddProduct: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Blue50),
                contentAlignment = Alignment.Center
            ) {
                // Inner gradient box
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(Blue600, Blue500))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Category, null,
                        tint     = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Sin productos aún",
                    fontSize      = 20.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Slate900,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    "Registra tu primer producto para comenzar a ver el catálogo aquí.",
                    fontSize  = 14.sp,
                    color     = Slate400,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick  = onAddProduct,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation    = 8.dp,
                        shape        = RoundedCornerShape(16.dp),
                        ambientColor = Blue600.copy(alpha = 0.20f),
                        spotColor    = Blue700.copy(alpha = 0.28f)
                    ),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(Icons.Outlined.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Agregar producto",
                    fontSize      = 15.sp,
                    fontWeight    = FontWeight.SemiBold,
                    letterSpacing = 0.1.sp
                )
            }
        }
    }
}
