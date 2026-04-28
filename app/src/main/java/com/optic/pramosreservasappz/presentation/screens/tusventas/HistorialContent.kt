package com.optic.pramosreservasappz.presentation.screens.tusventas

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
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.tusventas.components.HistorialSaleCard
import kotlinx.coroutines.launch

private val PrimaryPurple = Color(0xFF6E4FDB)
private val PrimaryBlue   = Color(0xFF3B78C4)
private val PrimaryGreen  = Color(0xFF10A37F)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistorialContent(
    modifier: Modifier = Modifier,
    sales: List<SaleResponse>,
    paddingValues: PaddingValues,
    viewModel: HistorialViewModel,
    navController: NavHostController
) {
    val query       by viewModel.searchQuery.collectAsState()
    val deleteState by viewModel.deleteSaleState
    val localSales  by viewModel.localSalesList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Venta eliminada ✓", duration = SnackbarDuration.Short) }
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

    val hasQuery      = query.isNotBlank()
    val filteredSales = remember(query, localSales) {
        if (query.isBlank()) localSales
        else localSales.filter { it.description?.contains(query, ignoreCase = true) == true }
    }

    val totalAmount    = remember(localSales) {
        localSales.sumOf { try { it.amount.toString().toDouble() } catch (e: Exception) { 0.0 } }
    }
    val totalAmountText = remember(totalAmount) { "Bs. %,.0f".format(totalAmount) }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color(0xFFF7F6FA))
    ) {
        if (localSales.isEmpty() && !hasQuery) {
            EmptySalesState { navController.navigate(ClientScreen.ABMCliente.createRoute(null, false)) }
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // ── Stats cards ──
                item {
                    AnimatedVisibility(
                        visible = localSales.isNotEmpty(),
                        enter   = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        StatsHeaderRow(totalSales = localSales.size, totalAmount = totalAmountText)
                    }
                }

                // ── Fila compacta: pill "RECIENTES" + buscador con contador ──
                item {
                    SearchAndPillRow(
                        query         = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        hasQuery      = hasQuery,
                        totalCount    = localSales.size,
                        filteredCount = filteredSales.size
                    )
                    Spacer(Modifier.height(6.dp))
                }

                // ── Lista / estado vacío de búsqueda ──
                if (hasQuery && filteredSales.isEmpty()) {
                    item { SearchEmptyState(query = query) }
                } else {
                    items(items = filteredSales, key = { it.id }) { sale ->
                        HistorialSaleCard(
                            sale          = sale,
                            navController = navController,
                            onDelete      = { viewModel.deleteSaleSoft(sale.id) },
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData   = data,
                containerColor = Color(0xFF1A1A2E),
                contentColor   = Color.White,
                shape          = RoundedCornerShape(14.dp)
            )
        }

        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(),
            exit     = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 8.dp) {
                Box(Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryPurple, strokeWidth = 2.5.dp, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
// Pill "RECIENTES" + buscador compacto + contador en una fila
// ─────────────────────────────────────────────────────────
@Composable
private fun SearchAndPillRow(
    query: String,
    onQueryChange: (String) -> Unit,
    hasQuery: Boolean,
    totalCount: Int,
    filteredCount: Int
) {
    val focusManager = LocalFocusManager.current
    var isFocused    by remember { mutableStateOf(false) }

    val pillColor = if (hasQuery) PrimaryBlue else PrimaryPurple
    val pillLabel = if (hasQuery) "RESULTADOS" else "RECIENTES"
    val pillIcon  = if (hasQuery) Icons.Outlined.Search else Icons.Outlined.History
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
            Text(pillLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                color = pillColor, letterSpacing = 0.5.sp)
        }

        // ── Buscador compacto con badge de contador ──
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
            Icon(Icons.Outlined.Search, null,
                tint     = if (isFocused) pillColor else Color(0xFFBBBBCC),
                modifier = Modifier.size(13.dp))

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
                        if (query.isEmpty()) Text("Buscar...", fontSize = 12.sp, color = Color(0xFFBBBBCC))
                        inner()
                    }
                }
            )

            // Badge contador (animated)
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
                    Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                        color = pillColor, letterSpacing = 0.2.sp)
                }
            }

            // X limpiar búsqueda
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
private fun StatsHeaderRow(totalSales: Int, totalAmount: String) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(Modifier.weight(1f), "$totalSales", "VENTAS", Icons.Outlined.Receipt, PrimaryPurple, Color(0xFFF0EEFF))
        StatCard(Modifier.weight(1f), totalAmount,   "TOTAL",  Icons.Outlined.Payments, PrimaryGreen, Color(0xFFEAF7F3), 15)
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    value: String,
    label: String,
    icon: ImageVector,
    accentColor: Color,
    bgColor: Color,
    valueFontSize: Int = 26
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(18.dp), color = bgColor) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier.size(28.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Icon(icon, null, tint = accentColor, modifier = Modifier.size(14.dp)) }
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor, letterSpacing = 0.8.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = valueFontSize.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E), letterSpacing = (-0.5).sp, maxLines = 1)
        }
    }
}

@Composable
private fun SearchEmptyState(query: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFFF0EEFF)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.SearchOff, null, tint = PrimaryPurple, modifier = Modifier.size(32.dp)) }
        Text("Sin resultados", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
        Text("No hay ventas que coincidan con \"$query\"",
            fontSize = 13.sp, color = Color(0xFFAAAABB), textAlign = TextAlign.Center)
    }
}

@Composable
private fun EmptySalesState(onAddSale: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp).clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFF0EEFF), Color(0xFFEAF0FF)))),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.Receipt, null, tint = PrimaryPurple, modifier = Modifier.size(40.dp)) }
            Text("Sin ventas aún", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E), letterSpacing = (-0.5).sp)
            Text("Registra tu primera venta para comenzar a ver el historial aquí.",
                fontSize = 14.sp, color = Color(0xFFAAAABB), textAlign = TextAlign.Center, lineHeight = 20.sp)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick  = onAddSale,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Icon(Icons.Outlined.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Nueva venta", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
