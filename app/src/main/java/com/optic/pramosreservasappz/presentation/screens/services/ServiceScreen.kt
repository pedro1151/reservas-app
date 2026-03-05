package com.optic.pramosreservasappz.presentation.screens.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PullRefreshWrapper
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: ServiceViewModel = hiltViewModel()
    val serviceResource by viewModel.servicesState.collectAsState()
    val localizedContext = LocalizedContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getServicesByProvider(1)
    }

    LaunchedEffect(serviceResource) {
        if (serviceResource !is Resource.Loading) isRefreshing = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Servicios y clases",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        ClientScreen.ABMServicio.createRoute(serviceId = null, editable = false)
                    )
                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(22.dp))
            }
        },
        containerColor = Color.White
    ) { paddingValues ->

        PullRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.getServicesByProvider(1)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = serviceResource) {
                is Resource.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Color.Black, strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                is Resource.Success -> {
                    ServiceContent(
                        services = result.data,
                        paddingValues = PaddingValues(0.dp),
                        viewModel = viewModel,
                        navController = navController,
                        isAuthenticated = isAuthenticated,
                        localizedContext = localizedContext
                    )
                }
                is Resource.Failure -> {
                    ErrorState(onRetry = { isRefreshing = true; viewModel.getServicesByProvider(1) })
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = Color(0xFFF5F5F5)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.WifiOff, null, tint = Color(0xFFCCCCCC), modifier = Modifier.size(28.dp))
                }
            }
            Text("Sin conexión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text("Jala hacia abajo para reintentar", fontSize = 13.sp, color = Color(0xFFAAAAAA))
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A)),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text("Reintentar", fontSize = 13.sp, color = Color.White)
            }
        }
    }
}
