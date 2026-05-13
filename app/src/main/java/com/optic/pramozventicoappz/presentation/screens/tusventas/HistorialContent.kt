package com.optic.pramozventicoappz.presentation.screens.tusventas

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.R
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import com.optic.pramozventicoappz.domain.model.sales.SaleResponse
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.tusventas.components.HistorialSaleCard
import com.optic.pramozventicoappz.presentation.screens.tusventas.components.HistorialSaleGridCard
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

private val PrimaryPink = Color(0xFFE91E63)
private val TextPrimarySoft = Color(0xFF334155)
private val TextSecondarySoft = Color(0xFF64748B)
private val BorderGray = Color(0xFFE5E7EB)

private enum class SaleQuickFilter {
    RECENT,
    SELLERS,
    TOP_AMOUNT
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistorialContent(
    modifier: Modifier = Modifier,
    sales: List<SaleResponse>,
    paddingValues: PaddingValues,
    viewModel: HistorialViewModel,
    navController: NavHostController
) {
    val query by viewModel.searchQuery.collectAsState()
    val deleteState by viewModel.deleteSaleState
    val localSales by viewModel.localSalesList.collectAsState()
    val viewType by viewModel.productViewType.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isDeleting by remember { mutableStateOf(false) }

    var selectedQuickFilter by remember { mutableStateOf(SaleQuickFilter.RECENT) }
    var selectedSeller by remember { mutableStateOf<String?>(null) }

    val sellers = remember(localSales) {
        localSales.mapNotNull {
            it.salesman?.username?.takeIf { name -> name.isNotBlank() }
                ?: it.salesman?.email?.takeIf { email -> email.isNotBlank() }
        }.distinct().sorted()
    }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Venta eliminada ✓",
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
            else -> isDeleting = false
        }
    }

    val hasQuery = query.isNotBlank()

    val filteredSales = remember(
        query,
        localSales,
        selectedSeller,
        selectedQuickFilter
    ) {
        val bySearch = if (query.isBlank()) {
            localSales
        } else {
            localSales.filter {
                it.description?.contains(query, ignoreCase = true) == true ||
                        it.client?.fullName?.contains(query, ignoreCase = true) == true ||
                        it.salesman?.username?.contains(query, ignoreCase = true) == true ||
                        it.salesman?.email?.contains(query, ignoreCase = true) == true
            }
        }

        val bySeller = selectedSeller?.let { seller ->
            bySearch.filter {
                it.salesman?.username == seller || it.salesman?.email == seller
            }
        } ?: bySearch

        when (selectedQuickFilter) {
            SaleQuickFilter.RECENT -> bySeller.sortedByDescending { parseSaleDateMillis(it.created) }
            SaleQuickFilter.SELLERS -> bySeller.sortedBy {
                it.salesman?.username ?: it.salesman?.email ?: ""
            }
            SaleQuickFilter.TOP_AMOUNT -> bySeller.sortedByDescending { saleAmountAsDouble(it.amount) }
        }
    }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF9FAFB),
                        Color(0xFFF1F5F9)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_claro),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.075f),
            contentScale = ContentScale.Crop
        )

        if (localSales.isEmpty() && !hasQuery) {
            EmptySalesState {
                navController.navigate(
                    ClientScreen.ABMCliente.createRoute(null, false)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 10.dp,
                    bottom = 110.dp
                )
            ) {
                item {
                    SalesFilterHeader(
                        query = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        hasQuery = hasQuery,
                        totalCount = localSales.size,
                        filteredCount = filteredSales.size,
                        viewMode = viewType,
                        onViewModeChange = { selectedType ->
                            viewModel.updateProductViewType(selectedType)
                        },
                        selectedQuickFilter = selectedQuickFilter,
                        onQuickFilterChange = { selectedQuickFilter = it },
                        sellers = sellers,
                        selectedSeller = selectedSeller,
                        onSellerSelected = { selectedSeller = it }
                    )

                    Spacer(Modifier.height(10.dp))
                }

                if ((hasQuery || selectedSeller != null) && filteredSales.isEmpty()) {
                    item {
                        SearchEmptyState(query = query)
                    }
                } else {
                    when (viewType) {
                        ProductViewType.LIST -> {
                            items(
                                items = filteredSales,
                                key = { it.id }
                            ) { sale ->
                                HistorialSaleCard(
                                    sale = sale,
                                    navController = navController,
                                    onDelete = { viewModel.deleteSaleSoft(sale.id) },
                                    modifier = Modifier.animateItemPlacement(
                                        spring(
                                            Spring.DampingRatioMediumBouncy,
                                            Spring.StiffnessLow
                                        )
                                    )
                                )
                            }
                        }

                        ProductViewType.GRID -> {
                            val chunkedSales = filteredSales.chunked(2)

                            items(
                                items = chunkedSales,
                                key = { it.first().id }
                            ) { pair ->
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
                                    pair.forEach { sale ->
                                        HistorialSaleGridCard(
                                            sale = sale,
                                            modifier = Modifier.weight(1f),
                                            navController = navController,
                                            onDelete = { viewModel.deleteSaleSoft(it.id) }
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFF0F172A),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

        AnimatedVisibility(
            visible = isDeleting,
            enter = fadeIn(animationSpec = tween(220)),
            exit = fadeOut(animationSpec = tween(180)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 10.dp
            ) {
                Box(
                    modifier = Modifier.padding(22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryPink,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SalesFilterHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    hasQuery: Boolean,
    totalCount: Int,
    filteredCount: Int,
    viewMode: ProductViewType,
    onViewModeChange: (ProductViewType) -> Unit,
    selectedQuickFilter: SaleQuickFilter,
    onQuickFilterChange: (SaleQuickFilter) -> Unit,
    sellers: List<String>,
    selectedSeller: String?,
    onSellerSelected: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SearchAndViewModeRow(
            query = query,
            onQueryChange = onQueryChange,
            hasQuery = hasQuery,
            totalCount = totalCount,
            filteredCount = filteredCount,
            viewMode = viewMode,
            onViewModeChange = onViewModeChange
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SaleFilterChip(
                text = "Reciente",
                selected = selectedQuickFilter == SaleQuickFilter.RECENT,
                onClick = { onQuickFilterChange(SaleQuickFilter.RECENT) }
            )

            SaleFilterChip(
                text = "Mayor venta",
                selected = selectedQuickFilter == SaleQuickFilter.TOP_AMOUNT,
                onClick = { onQuickFilterChange(SaleQuickFilter.TOP_AMOUNT) }
            )

            Spacer(Modifier.weight(1f))

            SellerLovButton(
                sellers = sellers,
                selectedSeller = selectedSeller,
                onSellerSelected = onSellerSelected
            )
        }
    }
}

@Composable
private fun SaleFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) PrimaryPink.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.90f),
        animationSpec = tween(180),
        label = "filterBg"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) PrimaryPink else TextSecondarySoft,
        animationSpec = tween(180),
        label = "filterText"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = if (selected) PrimaryPink.copy(alpha = 0.22f) else BorderGray,
                shape = RoundedCornerShape(999.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 11.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
private fun SellerLovButton(
    sellers: List<String>,
    selectedSeller: String?,
    onSellerSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .height(36.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.92f))
                .border(1.dp, BorderGray, RoundedCornerShape(999.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = true
                }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.PersonSearch,
                contentDescription = null,
                tint = PrimaryPink,
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(5.dp))

            Text(
                text = selectedSeller ?: "Vendedor",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondarySoft,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 86.dp)
            )

            Spacer(Modifier.width(3.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondarySoft,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Todos",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimarySoft
                    )
                },
                onClick = {
                    expanded = false
                    onSellerSelected(null)
                }
            )

            sellers.forEach { seller ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = seller,
                            fontSize = 13.sp,
                            color = TextPrimarySoft,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        expanded = false
                        onSellerSelected(seller)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchAndViewModeRow(
    query: String,
    onQueryChange: (String) -> Unit,
    hasQuery: Boolean,
    totalCount: Int,
    filteredCount: Int,
    viewMode: ProductViewType,
    onViewModeChange: (ProductViewType) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    val badgeText = if (hasQuery) "$filteredCount/$totalCount" else "$totalCount"

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.94f))
                .border(
                    width = if (isFocused) 1.2.dp else 1.dp,
                    color = if (isFocused) PrimaryPink.copy(alpha = 0.40f) else BorderGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = if (isFocused) PrimaryPink else Color(0xFF94A3B8),
                modifier = Modifier.size(16.dp)
            )

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = TextPrimarySoft,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(PrimaryPink),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
                decorationBox = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Buscar venta...",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        inner()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(PrimaryPink.copy(alpha = 0.09f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryPink
                )
            }

            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = scaleIn(tween(180)) + fadeIn(tween(180)),
                exit = scaleOut(tween(120)) + fadeOut(tween(120))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Limpiar",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier
                        .size(15.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onQueryChange("")
                            focusManager.clearFocus()
                        }
                )
            }
        }

        ViewModeSwitch(
            viewMode = viewMode,
            onViewModeChange = onViewModeChange
        )
    }
}

@Composable
private fun ViewModeSwitch(
    viewMode: ProductViewType,
    onViewModeChange: (ProductViewType) -> Unit
) {
    Row(
        modifier = Modifier
            .height(44.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.94f))
            .padding(3.dp)
    ) {
        ViewModeButton(
            icon = Icons.Outlined.ViewList,
            isSelected = viewMode == ProductViewType.LIST,
            onClick = { onViewModeChange(ProductViewType.LIST) }
        )

        ViewModeButton(
            icon = Icons.Outlined.GridView,
            isSelected = viewMode == ProductViewType.GRID,
            onClick = { onViewModeChange(ProductViewType.GRID) }
        )
    }
}

@Composable
private fun ViewModeButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryPink else Color.Transparent,
        animationSpec = tween(180),
        label = "viewModeBg"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF94A3B8),
        animationSpec = tween(180),
        label = "viewModeTint"
    )

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}

private fun saleAmountAsDouble(raw: Any?): Double {
    return try {
        raw.toString().toDouble()
    } catch (e: Exception) {
        0.0
    }
}

private fun parseSaleDateMillis(rawDate: String): Long {
    return try {
        OffsetDateTime.parse(rawDate).toInstant().toEpochMilli()
    } catch (e: Exception) {
        0L
    }
}

@Composable
private fun SearchEmptyState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(PrimaryPink.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = PrimaryPink,
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "Sin resultados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimarySoft
        )

        Text(
            text = if (query.isBlank()) {
                "No hay ventas para el filtro seleccionado"
            } else {
                "No hay ventas que coincidan con \"$query\""
            },
            fontSize = 13.sp,
            color = TextSecondarySoft,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun EmptySalesState(onAddSale: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF9FAFB),
                        Color(0xFFF1F5F9)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_claro),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.075f),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(PrimaryPink.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = null,
                    tint = PrimaryPink,
                    modifier = Modifier.size(42.dp)
                )
            }

            Text(
                text = "Sin ventas aún",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimarySoft,
                letterSpacing = (-0.6).sp
            )

            Text(
                text = "Registra tu primera venta para comenzar a ver tu historial.",
                fontSize = 14.sp,
                color = TextSecondarySoft,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onAddSale,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPink
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Nueva venta",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}