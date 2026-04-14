package com.optic.pramosreservasappz.presentation.screens.productos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.components.PullRefreshWrapper
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

private val PrimaryPurple = Color(0xFF6E4FDB)
private val PrimaryBlue   = Color(0xFF3B78C4)
private val DeepNavy      = Color(0xFF1A1A2E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController   : NavHostController,
    isAuthenticated : Boolean = false
) {
    val viewModel        : ProductViewModel = hiltViewModel()
    val productResource  by viewModel.productsState.collectAsState()
    val localizedContext = LocalizedContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts(ownerId = 1, name = "")
    }

    LaunchedEffect(productResource) {
        if (productResource !is Resource.Loading) isRefreshing = false
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                title         = "PRODUCTOS Y SERVICIOS",
                navController = navController
            )
        },
        floatingActionButton = {
            // FAB con gradiente estilo Historial
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryPurple, PrimaryBlue)
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) {
                        navController.navigate(
                            ClientScreen.ABMServicio.createRoute(serviceId = null, editable = false)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo producto",
                    tint     = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color(0xFFF7F6FA)
    ) { paddingValues ->

        PullRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh    = {
                isRefreshing = true
                viewModel.loadProducts(ownerId = 1, name = "")
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = productResource) {
                is Resource.Loading -> {
                    ProductLoadingState()
                }
                is Resource.Success -> {
                    ProductContent(
                        products         = result.data,
                        paddingValues    = PaddingValues(0.dp),
                        viewModel        = viewModel,
                        navController    = navController,
                        isAuthenticated  = isAuthenticated,
                        localizedContext = localizedContext
                    )
                }
                is Resource.Failure -> {
                    ProductErrorState(
                        onRetry = { isRefreshing = true; viewModel.loadProducts(ownerId = 1, name = "") }
                    )
                }
                else -> {
                    ProductLoadingState()
                }
            }
        }
    }
}

// ── Loading estilizado ──
@Composable
private fun ProductLoadingState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color       = PrimaryPurple,
                strokeWidth = 3.dp,
                modifier    = Modifier.size(36.dp)
            )
            Text(
                "Cargando productos...",
                fontSize   = 13.sp,
                color      = Color(0xFFAAAABB),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Error state estilizado ──
@Composable
private fun ProductErrorState(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFFFF0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.WifiOff, null,
                    tint     = Color(0xFFE05C5C),
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                "Sin conexión",
                fontSize      = 20.sp,
                fontWeight    = FontWeight.Bold,
                color         = DeepNavy,
                letterSpacing = (-0.4).sp
            )
            Text(
                "Revisa tu conexión a internet y vuelve a intentarlo.",
                fontSize   = 14.sp,
                color      = Color(0xFFAAAABB),
                textAlign  = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(4.dp))

            Button(
                onClick  = onRetry,
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    Icons.Outlined.Refresh, null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Reintentar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
