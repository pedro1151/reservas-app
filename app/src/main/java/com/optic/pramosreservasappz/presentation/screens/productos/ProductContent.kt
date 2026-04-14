package com.optic.pramosreservasappz.presentation.screens.productos

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.productos.components.PrincipalProducCard
import kotlinx.coroutines.launch

private val PrimaryPurple = Color(0xFF6E4FDB)
private val PrimaryBlue   = Color(0xFF3B78C4)
private val PrimaryGreen  = Color(0xFF10A37F)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductContent(
    modifier         : Modifier = Modifier,
    products         : List<ProductResponse>,
    paddingValues    : PaddingValues,
    viewModel        : ProductViewModel,
    navController    : NavHostController,
    isAuthenticated  : Boolean = false,
    localizedContext : Context
) {
    val query          by viewModel.searchQuery.collectAsState()
    val deleteState    by viewModel.deleteProductState
    val localProducts  by viewModel.localProductsList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Producto eliminado ✓", duration = SnackbarDuration.Short) }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch { snackbarHostState.showSnackbar("Error: ${state.message}", duration = SnackbarDuration.Long) }
                isDeleting = false
            }
            is Resource.Loading -> isDeleting = true
            else                -> isDeleting = false
        }
    }

    val hasQuery         = query.isNotBlank()
    val filteredProducts = remember(query, localProducts) {
        if (query.isBlank()) localProducts
        else localProducts.filter { it.name.contains(query, ignoreCase = true) }
    }

    val totalValue = remember(localProducts) {
        localProducts.sumOf {
            try { it.price.toString().toDouble() } catch (e: Exception) { 0.0 }
        }
    }
    val totalValueText = remember(totalValue) { "Bs. %,.0f".format(totalValue) }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFF7F6FA))
    ) {
        if (localProducts.isEmpty() && !hasQuery) {
            EmptyProductsState {
                navController.navigate(
                    ClientScreen.ABMServicio.createRoute(serviceId = null, editable = false)
                )
            }
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // ── Stats cards ──
                item {
                    AnimatedVisibility(
                        visible = localProducts.isNotEmpty(),
                        enter   = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        ProductStatsHeaderRow(
                            totalProducts = localProducts.size,
                            totalValue    = totalValueText
                        )
                    }
                }

                // ── Pill + buscador compacto + contador ──
                item {
                    ProductSearchAndPillRow(
                        query         = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        hasQuery      = hasQuery,
                        totalCount    = localProducts.size,
                        filteredCount = filteredProducts.size
                    )
                    Spacer(Modifier.height(6.dp))
                }

                // ── Lista / estado vacío de búsqueda ──
                if (hasQuery && filteredProducts.isEmpty()) {
                    item { ProductSearchEmptyState(query = query) }
                } else {
                    items(items = filteredProducts, key = { it.id }) { product ->
                        PrincipalProducCard(
                            product       = product,
                            navController = navController,
                            onDelete      = { viewModel.deleteProduct(it.id) },
                            modifier      = Modifier
                                .animateItemPlacement(
                                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
                                )
                                .padding(horizontal = 16.dp)
                        )
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
                snackbarData   = data,
                containerColor = Color(0xFF1A1A2E),
                contentColor   = Color.White,
                shape          = RoundedCornerShape(14.dp)
            )
        }

        // ── Overlay de carga al eliminar ──
        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(),
            exit     = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape          = RoundedCornerShape(16.dp),
                color          = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color       = PrimaryPurple,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Pill "CATÁLOGO" + buscador compacto + contador
// ─────────────────────────────────────────────────────────
@Composable
private fun ProductSearchAndPillRow(
    query         : String,
    onQueryChange : (String) -> Unit,
    hasQuery      : Boolean,
    totalCount    : Int,
    filteredCount : Int
) {
    val focusManager = LocalFocusManager.current
    var isFocused    by remember { mutableStateOf(false) }

    val pillColor = if (hasQuery) PrimaryBlue else PrimaryPurple
    val pillLabel = if (hasQuery) "RESULTADOS" else "CATÁLOGO"
    val pillIcon  = if (hasQuery) Icons.Outlined.Search else Icons.Outlined.Category
    val badgeText = if (hasQuery) "$filteredCount/$totalCount" else "$totalCount"

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── Pill izquierdo ──
        Row(
            modifier = Modifier
                .background(pillColor.copy(alpha = 0.10f), RoundedCornerShape(20.dp))
                .padding(horizontal = 11.dp, vertical = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(pillIcon, null, tint = pillColor, modifier = Modifier.size(11.dp))
            Text(
                pillLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                color = pillColor, letterSpacing = 0.5.sp
            )
        }

        // ── Buscador compacto ──
        Row(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    width = if (isFocused) 1.dp else 0.5.dp,
                    color = if (isFocused) pillColor.copy(alpha = 0.45f) else Color(0xFFE5E5EE),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(
                Icons.Outlined.Search, null,
                tint     = if (isFocused) pillColor else Color(0xFFBBBBCC),
                modifier = Modifier.size(13.dp)
            )

            BasicTextField(
                value          = query,
                onValueChange  = onQueryChange,
                modifier       = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle      = TextStyle(fontSize = 12.sp, color = Color(0xFF1A1A2E)),
                cursorBrush    = SolidColor(pillColor),
                singleLine     = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                decorationBox  = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text("Buscar...", fontSize = 12.sp, color = Color(0xFFBBBBCC))
                        }
                        inner()
                    }
                }
            )

            // Badge contador animado
            AnimatedContent(
                targetState  = badgeText,
                transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(130)) },
                label        = "badge_counter"
            ) { label ->
                Box(
                    modifier = Modifier
                        .background(pillColor.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        label, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        color = pillColor, letterSpacing = 0.2.sp
                    )
                }
            }

            // X para limpiar búsqueda
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter   = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit    = scaleOut(tween(100)) + fadeOut(tween(100))
            ) {
                Icon(
                    Icons.Outlined.Close, "Limpiar",
                    tint     = Color(0xFFBBBBCC),
                    modifier = Modifier
                        .size(13.dp)
                        .clickable(remember { MutableInteractionSource() }, null) {
                            onQueryChange(""); focusManager.clearFocus()
                        }
                )
            }
        }
    }
}

// ── Stats cards ──
@Composable
private fun ProductStatsHeaderRow(totalProducts: Int, totalValue: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProductStatCard(
            modifier    = Modifier.weight(1f),
            value       = "$totalProducts",
            label       = "PRODUCTOS",
            icon        = Icons.Outlined.Category,
            accentColor = PrimaryPurple,
            bgColor     = Color(0xFFF0EEFF)
        )
        ProductStatCard(
            modifier       = Modifier.weight(1f),
            value          = totalValue,
            label          = "VALOR TOTAL",
            icon           = Icons.Outlined.Payments,
            accentColor    = PrimaryGreen,
            bgColor        = Color(0xFFEAF7F3),
            valueFontSize  = 15
        )
    }
}

@Composable
private fun ProductStatCard(
    modifier      : Modifier,
    value         : String,
    label         : String,
    icon          : ImageVector,
    accentColor   : Color,
    bgColor       : Color,
    valueFontSize : Int = 26
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = bgColor) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = accentColor, modifier = Modifier.size(14.dp))
                }
                Text(
                    label, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    color = accentColor, letterSpacing = 0.8.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                value, fontSize = valueFontSize.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E), letterSpacing = (-0.5).sp, maxLines = 1
            )
        }
    }
}

// ── Estado vacío de búsqueda ──
@Composable
private fun ProductSearchEmptyState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0EEFF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.SearchOff, null, tint = PrimaryPurple, modifier = Modifier.size(32.dp))
        }
        Text(
            "Sin resultados", fontSize = 16.sp,
            fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E)
        )
        Text(
            "No hay productos que coincidan con \"$query\"",
            fontSize = 13.sp, color = Color(0xFFAAAABB), textAlign = TextAlign.Center
        )
    }
}

// ── Estado vacío sin productos ──
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
                    .size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFF0EEFF), Color(0xFFEAF0FF)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Category, null, tint = PrimaryPurple, modifier = Modifier.size(40.dp))
            }
            Text(
                "Sin productos aún", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E), letterSpacing = (-0.5).sp
            )
            Text(
                "Registra tu primer producto para comenzar a ver el catálogo aquí.",
                fontSize = 14.sp, color = Color(0xFFAAAABB),
                textAlign = TextAlign.Center, lineHeight = 20.sp
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick  = onAddProduct,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Icon(Icons.Outlined.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Nuevo producto", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
