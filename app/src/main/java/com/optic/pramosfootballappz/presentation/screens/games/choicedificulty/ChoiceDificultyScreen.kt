package com.optic.pramosfootballappz.presentation.screens.games.choicedificulty

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.ProgressBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.presentation.components.BackTopBar
import com.optic.pramosfootballappz.presentation.screens.games.GameViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChoiceDificultyScreen(
    gameCode: String,
    navController: NavHostController
) {
    val viewModel: GameViewModel = hiltViewModel()
    val dificultyState by viewModel.dificultyState.collectAsState()

    // Dispara la carga de dificultades al entrar
    LaunchedEffect(Unit) {
        viewModel.getDificultys()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Seleccionar Dificultad"
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (dificultyState) {
                is Resource.Loading -> {
                    // Mostrar ProgressBar centrado
                    ProgressBar()
                }

                is Resource.Failure -> {
                    // Mostrar mensaje de error
                    Text(
                        text = (dificultyState as Resource.Failure).message ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                is Resource.Success -> {
                    val difficulties = (dificultyState as Resource.Success).data ?: emptyList()

                    if (difficulties.isEmpty()) {
                        Text(
                            text = "No hay dificultades disponibles",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        // Mostrar selector de dificultades
                        GameDificultySelector(
                            difficulties = difficulties,
                            gameCode=gameCode,
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}