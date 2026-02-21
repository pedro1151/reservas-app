package com.optic.pramosreservasappz.presentation.screens.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

    LaunchedEffect(Unit) {
        viewModel.getServicesByProvider(1)
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
        when (val result = serviceResource) {
            is Resource.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            is Resource.Success -> {
                ServiceContent(
                    services = result.data,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    localizedContext = localizedContext
                )
            }
            is Resource.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "No se pudo cargar los servicios",
                            color = Color(0xFF9E9E9E),
                            fontSize = 15.sp
                        )
                        TextButton(onClick = { viewModel.getServicesByProvider(1) }) {
                            Text("Reintentar", color = Color.Black)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
