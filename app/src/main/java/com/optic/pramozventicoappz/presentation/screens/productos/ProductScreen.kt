package com.optic.pramozventicoappz.presentation.screens.productos

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.PrimaryTopBar
import com.optic.pramozventicoappz.presentation.components.PullRefreshWrapper
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer
import com.optic.pramozventicoappz.presentation.settings.idiomas.LocalizedContext

// ─── Design Tokens ─────────────────────────────────────────────────────────────
private val Blue700   = Color(0xFF1D4ED8)
private val Blue600   = Color(0xFF2563EB)
private val Blue500   = Color(0xFF3B82F6)
private val Blue50    = Color(0xFFEFF6FF)
private val Slate900  = Color(0xFF0F172A)
private val Slate400  = Color(0xFF94A3B8)
private val PageBg    = Color(0xFFF8FAFC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController   : NavHostController,
    isAuthenticated : Boolean = false,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()
    // DATOS DE LA SESSION
    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode


    val viewModel        : ProductViewModel = hiltViewModel()
    val productResource  by viewModel.productsState.collectAsState()
    val localizedContext = LocalizedContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadProducts(businessId = businessId, name = "")
        }
    }

    LaunchedEffect(productResource) {
        if (productResource !is Resource.Loading) isRefreshing = false
    }
    SalesScreenWithDrawer(navController) { onMenuClick ->
        Scaffold(
            topBar = {

                PrimaryTopBar(
                    title = "Productos y servicios",
                    navController = navController,
                    onMenuClick = onMenuClick
                )

            },
            floatingActionButton = {
                // ── Gradient Extended FAB ──
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Blue600.copy(alpha = 0.25f),
                            spotColor = Blue700.copy(alpha = 0.35f)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(listOf(Blue600, Blue500))
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate(
                                ClientScreen.ABMServicio.createRoute(
                                    serviceId = null,
                                    editable = false
                                )
                            )
                        }
                        .padding(horizontal = 22.dp, vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            "Nuevo producto",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.1.sp
                        )
                    }
                }
            },
            containerColor = PageBg
        ) { paddingValues ->

            PullRefreshWrapper(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.loadProducts(businessId = 1, name = "")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val result = productResource) {
                    is Resource.Loading -> ProductLoadingState()
                    is Resource.Success -> {
                        ProductContent(
                            products = result.data,
                            paddingValues = PaddingValues(0.dp),
                            viewModel = viewModel,
                            navController = navController,
                            isAuthenticated = isAuthenticated,
                            localizedContext = localizedContext
                        )
                    }

                    is Resource.Failure -> {
                        ProductErrorState(
                            onRetry = {
                                isRefreshing = true
                                viewModel.loadProducts(businessId = 1, name = "")
                            }
                        )
                    }

                    else -> ProductLoadingState()
                }
            }
        }
    }
}

// ── Polished Loading State ──────────────────────────────────────────────────────
@Composable
private fun ProductLoadingState() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue   = 0.4f,
        targetValue    = 1f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier            = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Blue50, Color(0xFFDBEAFE))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color       = Blue600.copy(alpha = shimmerAlpha),
                    strokeWidth = 2.5.dp,
                    modifier    = Modifier.size(30.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Cargando catálogo",
                    fontSize      = 17.sp,
                    fontWeight    = FontWeight.Bold,
                    color         = Slate900,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    "Obteniendo tus productos...",
                    fontSize = 13.sp,
                    color    = Slate400
                )
            }
            // Skeleton bars
            Column(
                modifier            = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (i == 2) 0.6f else 1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFE2E8F0).copy(alpha = shimmerAlpha))
                    )
                }
            }
        }
    }
}

// ── Error State ─────────────────────────────────────────────────────────────────
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
                    .size(84.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFFEF2F2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.WifiOff, null,
                    tint     = Color(0xFFEF4444),
                    modifier = Modifier.size(38.dp)
                )
            }
            Text(
                "Sin conexión",
                fontSize      = 20.sp,
                fontWeight    = FontWeight.Bold,
                color         = Slate900,
                letterSpacing = (-0.4).sp
            )
            Text(
                "Revisa tu conexión a internet y vuelve a intentarlo.",
                fontSize   = 14.sp,
                color      = Slate400,
                textAlign  = TextAlign.Center,
                lineHeight = 21.sp
            )
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = onRetry,
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Blue600),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Icon(Icons.Outlined.Refresh, null, modifier = Modifier.size(17.dp))
                Spacer(Modifier.width(8.dp))
                Text("Reintentar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
