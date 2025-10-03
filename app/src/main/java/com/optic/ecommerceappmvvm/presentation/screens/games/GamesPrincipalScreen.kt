package com.optic.ecommerceappmvvm.presentation.screens.games

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.components.ProgressBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamesPrincipalScreen(
    navController: NavHostController
) {
    val viewModel: GameViewModel = hiltViewModel()
    val gameState by viewModel.gameState.collectAsState()

    // Dispara la carga de juegos al entrar
    LaunchedEffect(Unit) {
        viewModel.getGames()
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                title = "Games",
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (gameState) {
                is Resource.Loading -> {
                    ProgressBar()
                }

                is Resource.Success -> {
                    val data = (gameState as Resource.Success).data
                    if (data.isNullOrEmpty()) {
                        // Estado vacío
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "No hay juegos disponibles",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.getGames() }) {
                                Text("Reintentar")
                            }
                        }
                    } else {
                        // Renderiza el contenido
                        GamesPrincipalContent(
                            paddingValues = paddingValues,
                            navController = navController,
                            gameState = gameState
                        )
                    }
                }

                is Resource.Failure -> {
                    val message = (gameState as Resource.Failure).message
                        ?: "Error desconocido al cargar los juegos"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.getGames() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}
