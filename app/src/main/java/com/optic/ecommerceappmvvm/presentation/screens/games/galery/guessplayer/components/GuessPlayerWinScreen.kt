package com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayer
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import kotlinx.coroutines.delay

import kotlin.random.Random

@Composable
fun GuessPlayerWinScreen(
    player: GuessPlayer,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var showContent by remember { mutableStateOf(false) }

    // ⏳ Mostrar pantalla después de 2 segundos
    LaunchedEffect(Unit) {
        delay(2000)
        showContent = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        // 🎉 Confetti animado
        if (showContent) {
            ConfettiEffect()
        }
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(800)) +
                    scaleIn(
                        initialScale = 0.5f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
            exit = fadeOut(animationSpec = tween(400)) + scaleOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                // 🏆 Card redonda del jugador ganador
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(player.photo),
                        contentDescription = player.firstname,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    // Cinta inferior con nombre del jugador
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF2E2E2E), // grafito oscuro
                                        Color(0xFF1B1B1B)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = player.firstname,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }


                // 🎮 Botones de acción
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Jugar otra vez
                    Button(
                        onClick = {
                            navController.popBackStack() // Quita pantalla Win
                            navController.navigate(Graph.GAME + "/ADIVJUG") // Reinicia juego
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jugar otra vez")
                    }

                    // Salir del juego
                    OutlinedButton(
                        onClick = {
                            navController.popBackStack(ClientScreen.Games.route, false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salir del juego")
                    }
                }
            }
        }
    }

}

@Composable
fun ConfettiEffect() {
    val confettiCount = 50
    val confettiColors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Magenta)
    val random = remember { Random(System.currentTimeMillis()) }

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(confettiCount) {
            val xOffset = remember { Animatable(random.nextFloat() * 1000f) }
            val yOffset = remember { Animatable(0f) }
            val color = confettiColors[random.nextInt(confettiColors.size)]

            LaunchedEffect(Unit) {
                yOffset.animateTo(
                    targetValue = 2000f,
                    animationSpec = tween(durationMillis = random.nextInt(1500, 2500))
                )
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(x = xOffset.value.dp, y = yOffset.value.dp)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}