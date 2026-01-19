package com.optic.pramosfootballappz.presentation.screens.games.choicedificulty


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.presentation.components.DefaultButton
import com.optic.pramosfootballappz.presentation.navigation.Graph

@Composable
fun GameDificultySelector(
    difficulties: List<GameDificulty>,
    gameCode: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selected by remember(key1 = navController.currentBackStackEntry?.destination?.route) {
        mutableStateOf<String?>(null)
    }

    val buttonColor = Color(0xFF00C853) // Verde lima moderno

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        difficulties.forEach { difficulty ->
            val isSelected = selected == difficulty.code

            // Animaciones suaves
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) buttonColor else Color.Transparent,
                animationSpec = tween(durationMillis = 300)
            )
            val borderWidth by animateDpAsState(
                targetValue = if (isSelected) 4.dp else 0.dp,
                animationSpec = tween(durationMillis = 300)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selected = difficulty.code },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = difficulty.code,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected ) buttonColor
                                        else Color.White
                            )
                            Text(
                                text = "${difficulty.points} pts",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { selected = difficulty.code },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = buttonColor
                            )
                        )
                    }

                    // Borde inferior animado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(borderWidth)
                            .background(borderColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            text = "CONTINUAR",
            enabled = selected != null,
            onClick = { navController.navigate("${Graph.GAME}/${gameCode}")},
            color = buttonColor,
            icon = Icons.Filled.ArrowForward
        )
    }
}
