package com.optic.pramosreservasappz.presentation.screens.tusventas

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductViewType
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.tusventas.components.HistorialSaleCard
import com.optic.pramosreservasappz.presentation.screens.tusventas.components.HistorialSaleGridCard
import kotlinx.coroutines.launch

private val PrimaryPink = Color(0xFFE91E63)
private val PrimaryPinkDark = Color(0xFFD81B60)

private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF475569)
private val BorderGray = Color(0xFFE5E7EB)
private val SoftGray = Color(0xFFF8FAFC)

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

    val filteredSales = remember(query, localSales) {
        if (query.isBlank()) {
            localSales
        } else {
            localSales.filter {
                it.description?.contains(query, ignoreCase = true) == true ||
                        it.client?.fullName?.contains(query, ignoreCase = true) == true ||
                        it.salesman?.username?.contains(query, ignoreCase = true) == true
            }
        }
    }

    val totalAmount = remember(localSales) {
        localSales.sumOf {
            try {
                it.amount.toString().toDouble()
            } catch (e: Exception) {
                0.0
            }
        }
    }

    val totalAmountText = remember(totalAmount) {
        "$ %,.0f".format(totalAmount)
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
        if (localSales.isEmpty() && !hasQuery) {
            EmptySalesState {
                navController.navigate(
                    ClientScreen.ABMCliente.createRoute(null, false)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {
                item {
                    SalesHeroHeader(
                        totalSales = localSales.size,
                        totalAmount = totalAmountText
                    )
                }

                item {
                    SearchAndPillRow(
                        query = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        hasQuery = hasQuery,
                        totalCount = localSales.size,
                        filteredCount = filteredSales.size,
                        viewMode = viewType,
                        onViewModeChange = { selectedType ->
                            viewModel.updateProductViewType(selectedType)
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                if (hasQuery && filteredSales.isEmpty()) {
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
                containerColor = TextPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

        AnimatedVisibility(
            visible = isDeleting,
            enter = fadeIn(),
            exit = fadeOut(),
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
private fun SalesHeroHeader(
    totalSales: Int,
    totalAmount: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 16.dp)
            .height(174.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-38).dp, y = 8.dp)
                .size(width = 280.dp, height = 122.dp)
                .clip(RoundedCornerShape(64.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            PrimaryPink.copy(alpha = 0.18f),
                            PrimaryPink.copy(alpha = 0.07f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 46.dp, y = (-2).dp)
                .size(width = 260.dp, height = 116.dp)
                .clip(RoundedCornerShape(62.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            PrimaryPinkDark.copy(alpha = 0.08f),
                            PrimaryPink.copy(alpha = 0.20f)
                        )
                    )
                )
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 5.dp,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxHeight()
                        .width(120.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    PrimaryPink.copy(alpha = 0.055f),
                                    PrimaryPinkDark.copy(alpha = 0.095f)
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    PrimaryPink.copy(alpha = 0.045f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Resumen",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeaderTotalCard(
                            modifier = Modifier.weight(1.75f),
                            value = totalAmount
                        )

                        HeaderSalesCard(
                            modifier = Modifier.weight(0.75f),
                            value = "$totalSales"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderTotalCard(
    modifier: Modifier,
    value: String
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(SoftGray)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            maxLines = 1
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = value,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            letterSpacing = (-0.4).sp
        )
    }
}

@Composable
private fun HeaderSalesCard(
    modifier: Modifier,
    value: String
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(SoftGray)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ventas",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            maxLines = 1
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun SearchAndPillRow(
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = if (isFocused) 1.2.dp else 1.dp,
                    color = if (isFocused) PrimaryPink.copy(alpha = 0.45f) else BorderGray,
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
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
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

            AnimatedContent(
                targetState = badgeText,
                transitionSpec = {
                    fadeIn(tween(180)) togetherWith fadeOut(tween(130))
                },
                label = "badge_counter"
            ) { label ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(PrimaryPink.copy(alpha = 0.10f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryPink,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit = scaleOut(tween(100)) + fadeOut(tween(100))
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
            .background(Color.White)
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
        animationSpec = tween(200),
        label = "viewModeBg"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF94A3B8),
        animationSpec = tween(200),
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
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )

        Text(
            text = "No hay ventas que coincidan con \"$query\"",
            fontSize = 13.sp,
            color = TextSecondary,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                PrimaryPink,
                                PrimaryPinkDark
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }

            Text(
                text = "Sin ventas aún",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                letterSpacing = (-0.6).sp
            )

            Text(
                text = "Registra tu primera venta para comenzar a ver tu historial y tus métricas.",
                fontSize = 14.sp,
                color = TextSecondary,
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
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}