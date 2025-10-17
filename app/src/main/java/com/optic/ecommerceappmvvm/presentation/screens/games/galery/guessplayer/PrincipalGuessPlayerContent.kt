package com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayer
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.SimilarPlayer
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PrincipalGuessPlayerContent(
    similarPlayers: List<SimilarPlayer>,
    targetPlayer: GuessPlayer,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PrincipalGuessPlayerVM,
    gameCode : String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {


        OvalLayout(similarPlayers = similarPlayers,
            targetPlayer = targetPlayer,
            navController=navController,
            viewModel = viewModel,
            gameCode = gameCode
        )
    }
}

@Composable
fun OvalLayout(similarPlayers: List<SimilarPlayer>,
               targetPlayer: GuessPlayer,
               navController: NavHostController,
               viewModel: PrincipalGuessPlayerVM,
               gameCode: String
) {
    Layout(
        content = {
            similarPlayers.forEach { player ->
                PlayerMiniCard(targetPlayer = targetPlayer,
                    player = player,
                    isTarget = player.id == targetPlayer.id,
                    navController=navController,
                    viewModel = viewModel,
                    gameCode = gameCode
                )
            }
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val centerX = width / 2
        val centerY = height / 2

        val a = width * 0.45f
        val b = height * 0.45f // menos puntiagudo

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val angle = (index.toFloat() / similarPlayers.size) * (2 * Math.PI)
                val x = centerX + (a * cos(angle)).toInt() - placeable.width / 2
                val y = centerY + (b * sin(angle)).toInt() - placeable.height / 2
                placeable.place(x, y)
            }
        }
    }
}

@Composable
fun PlayerMiniCard(targetPlayer: GuessPlayer,
                   player: SimilarPlayer,
                   isTarget: Boolean,
                   navController: NavHostController,
                   viewModel: PrincipalGuessPlayerVM,
                   gameCode: String
) {
    val context = LocalContext.current
    var isClicked by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    val scale by animateFloatAsState(
        targetValue = when {
            isClicked && isCorrect == true -> 1.30f
            isClicked && isCorrect == false -> 0.9f
            else -> 1f
        },
        animationSpec = tween(400)
    )

    val overlayColor by animateColorAsState(
        targetValue = when {
            isClicked && isCorrect == true -> Color(0x8000C853)
            isClicked && isCorrect == false -> Color(0x80D32F2F)
            else -> Color.Transparent
        },
        animationSpec = tween(400)
    )


    // 🔥 Efecto: cuando acierta, redirige después de 2 segundos
    LaunchedEffect(isCorrect) {
        if (isCorrect == true) {
            delay(3000) // esperar 2 segundos
            val json = Uri.encode(Gson().toJson(targetPlayer))
            navController.navigate("${Graph.GUESSPLAYER_WIN}/${gameCode}/$json")
        }
    }
    Box(
        modifier = Modifier
            .size(75.dp)
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = !isClicked) {
                isClicked = true
                if (isTarget) {
                    isCorrect = true
                    MediaPlayer.create(context, R.raw.correct_sound)?.start()

                } else {
                    isCorrect = false
                    MediaPlayer.create(context, R.raw.error_sound)?.start()
                    // resto puntos
                    viewModel.onWrongGuess()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Imagen principal
        Image(
            painter = rememberAsyncImagePainter(player.photo),
            contentDescription = player.firstname,
            modifier = Modifier
                .size(40.dp) // 👈 tamaño explícito de la imagen
                .matchParentSize()
                .clip(RoundedCornerShape(18.dp))
        )

        // Capa de color animada (verde o roja)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(overlayColor)
        )

        // Cinta inferior con nombre
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(24.dp)
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
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Ícono ✔️ o ❌ animado
        AnimatedVisibility(
            visible = isClicked,
            enter = fadeIn(tween(200)) + scaleIn(),
            exit = fadeOut(tween(200)) + scaleOut()
        ) {
            if (isCorrect == true) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Correcto",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Incorrecto",
                    tint = Color.Red,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}