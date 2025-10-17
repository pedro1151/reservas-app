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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayer
import com.optic.ecommerceappmvvm.domain.model.trivias.score.GameScoreCreate
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.games.GameViewModel
import com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer.PrincipalGuessPlayerVM
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GuessPlayerWinScreen(
    player: GuessPlayer,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PrincipalGuessPlayerVM,
    gameCode : String
) {
    var showContent by remember { mutableStateOf(false) }
    val score by viewModel.score

    //score
    val viewModel: GameViewModel = hiltViewModel()

    // 🧩 Armar el modelo para guardar
    val gameScore = remember(score) {
        GameScoreCreate(
            gameCode = gameCode,
            dificulty = "HARD",
            score = score,
            createdBy = "app"
        )
    }
    // GUARDO EL PUNTAJE
    LaunchedEffect(Unit) {
        viewModel.saveScore(gameScore)
    }

    // ⏳ Mostrar pantalla después de 2 segundos
    LaunchedEffect(Unit) {
        delay(2000)
        showContent = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F10)),
        contentAlignment = Alignment.Center
    ) {
        // 🎉 Confetti animado
        if (showContent) {
            SmallConfetti(count = 60)
        }

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(700)) +
                    scaleIn(initialScale = 0.85f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = fadeOut() + scaleOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                // 🏆 Título principal
                Text(
                    text = "¡Excelente!",
                    color = Color(0xFFFFD600),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Has adivinado correctamente",
                    color = Color(0xFFB5B5B8),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                // 📸 Imagen circular del jugador
                Box(
                    modifier = Modifier.size(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .shadow(30.dp, shape = CircleShape)
                            .background(Color.Transparent)
                    )

                    Card(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(CircleShape),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141414))
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(player.photo),
                                contentDescription = player.firstname,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFF2E2E2E), Color(0xFF1B1B1B))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = player.firstname,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // 🔸 Badge del puntaje
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 8.dp, y = 16.dp)
                    ) {
                        ScoreBadge(score = score)
                    }
                }

                Text(
                    text = "¡Buen trabajo! Guarda tu récord o juega otra vez para mejorar tu puntaje.",
                    color = Color(0xFF9A9A9D),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // 🎮 Botones
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(Graph.GAME + "/ADIVJUG")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jugar otra vez", color = Color.White)
                    }

                    OutlinedButton(
                        onClick = {
                            navController.popBackStack(ClientScreen.Games.route, false)
                        },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color(0xFF3A3A3A)),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFFBDBDBD))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salir del juego", color = Color(0xFFBDBDBD))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBadge(score: Int) {
    val pulse = rememberInfiniteTransition()
    val scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(900), repeatMode = RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .size(88.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFFFD600), Color(0xFFFFA000))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                color = Color(0xFF0F0F10),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                fontSize = 22.sp
            )
            Text(
                text = "pts",
                color = Color(0xFF0F0F10),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SmallConfetti(count: Int = 40) {
    val random = remember { Random(System.currentTimeMillis()) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 📏 convertir dp a px
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(count) { i ->
            val startX = random.nextFloat() * screenWidthPx
            val color = listOf(
                Color(0xFFFFD600),
                Color(0xFF00C853),
                Color(0xFF00BCD4),
                Color(0xFFFF6D00)
            )[random.nextInt(4)]

            val size = listOf(6.dp, 8.dp, 10.dp)[random.nextInt(3)]
            val yOffset = remember { Animatable(0f) }
            val delayMs = random.nextInt(0, 1000)
            val duration = random.nextInt(2500, 4000)

            LaunchedEffect(i) {
                delay(delayMs.toLong())
                yOffset.animateTo(
                    targetValue = screenHeightPx,
                    animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                )
            }

            Box(
                modifier = Modifier
                    .size(size)
                    .offset {
                        IntOffset(startX.toInt(), yOffset.value.toInt())
                    }
                    .background(color = color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
