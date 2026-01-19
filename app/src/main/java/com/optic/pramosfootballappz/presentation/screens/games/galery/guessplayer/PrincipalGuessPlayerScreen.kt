package com.optic.pramosfootballappz.presentation.screens.games.galery.guessplayer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.ProgressBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.presentation.components.BackTopBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrincipalGuessPlayerScreen(
    navController: NavHostController,
    viewModel: PrincipalGuessPlayerVM,
    gameCode : String
) {
    val guessPlayerState by viewModel.guessPlayerState.collectAsState()
    // 🧠 Observa el estado del puntaje

    // Dispara la carga de dificultades al entrar y resetea al score
    LaunchedEffect(Unit) {
        viewModel.getGuessPlayer(19)
        viewModel.resetScore()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Adivina el Jugador"
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (guessPlayerState) {
                is Resource.Loading -> {
                    // Mostrar ProgressBar centrado
                    ProgressBar()
                }

                is Resource.Failure -> {
                    // Mostrar mensaje de error
                    Text(
                        text = (guessPlayerState as Resource.Failure).message ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                is Resource.Success -> {
                    val guessResponse = (guessPlayerState as Resource.Success).data

                    if (guessResponse == null || guessResponse.similars.isEmpty()) {
                        Text(
                            text = "No hay jugadores similares disponibles",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val similarPlayers = guessResponse.similars
                        val targetPlayer = guessResponse.target

                        // Ejemplo: Mostrar el nombre del jugador objetivo arriba
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {
                            /*
                            Text(
                                text = "Jugador objetivo: ${targetPlayer.firstname}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                             */

                            Spacer(modifier = Modifier.height(16.dp))

                            // Llamar a tu componente con los similares
                            PrincipalGuessPlayerContent(
                                similarPlayers = similarPlayers,
                                targetPlayer = targetPlayer,
                                navController = navController,
                                modifier = Modifier.fillMaxSize(),
                                viewModel = viewModel,
                                gameCode = gameCode
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}