package com.optic.pramosfootballappz.presentation.screens.prode.item

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse
import com.optic.pramosfootballappz.presentation.screens.prode.ProdeViewModel
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun ProdePredictRow(
    fixture: FixtureResponse,
    vm: ProdeViewModel
) {

    // para idioma
    val localizedContext = LocalizedContext.current

    val predictionSet = vm.userPredictions.collectAsState().value[fixture.id]
        ?: ProdeViewModel.UserPrediction()

    var pressed by remember  { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.End
    ) {

        // Animación para expandir/contraer la columna
        AnimatedContent(
            targetState = vm.isEditing.value,
            label = "editModeTransition",
            transitionSpec = {
                fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
            }
        ) { isEditing ->

            Column(
                modifier = Modifier
                    //.fillMaxWidth(0.60f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    //.background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.05f))
                    .animateContentSize()   // <--- animación automática de tamaño
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // ----------------------------
                // TITULO + PREDICCIÓN
                // ----------------------------
                Row(verticalAlignment = Alignment.CenterVertically) {
                   /*
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Tu prediccion",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFFFFC857)
                            )
                        )
                    }

                    */

                    Spacer(modifier = Modifier.width(6.dp))

                    Crossfade(targetState = isEditing, label = "predictionMode") { editing ->

                        if (editing) {
                            // ▼ Editar
                            var expanded by remember { mutableStateOf(false) }

                            Box {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    val displayText = predictionSet.prediction ?: localizedContext.getString(
                                        R.string.prode_mispredicciones_screen_seleccionar_resultado_label)

                                    // Colores modernos
                                    val textColor = when(displayText) {
                                        localizedContext.getString(
                                            R.string.prode_mispredicciones_screen_resultado_valor_gana) -> Color(0xFF4A90E2) // Azul suave
                                        localizedContext.getString(
                                            R.string.prode_mispredicciones_screen_resultado_valor_visita) -> Color(0xFF4CAF50) // Verde suave
                                        else -> Color.White
                                    }

                                    Text(
                                        text = displayText,
                                        fontSize = 10.sp,
                                        color = textColor
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Icono de dropdown
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint = textColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    listOf(
                                        localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_gana),
                                        localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_empate),
                                        localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_visita)
                                    ).forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                val color = when(option) {
                                                    localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_gana) -> Color(0xFF4A90E2)
                                                    localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_visita)-> Color(0xFF4CAF50)
                                                    else -> Color.White
                                                }
                                                Text(option, color = color)
                                            },
                                            onClick = {
                                                vm.updatePrediction(fixture.id!!, option)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                        } else {
                            // ► Solo lectura
                            val displayText = predictionSet.prediction ?: "-"
                            val textColor = when(displayText) {
                                localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_gana) -> Color(0xFF4A90E2)
                                localizedContext.getString(R.string.prode_mispredicciones_screen_resultado_valor_visita) -> Color(0xFF4CAF50)
                                localizedContext.getString(R.string.prode_mispredicciones_screen_seleccionar_resultado_label)-> Color.White
                                else -> Color.Red
                            }
                            Text(
                                text = displayText,
                                fontSize = 10.sp,
                                color = textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                // ----------------------------
                // TABLA DE GOLES (UNA SOLA FILA)
                // ----------------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // ======================
                    // GOLES LOCAL
                    // ======================
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AnimatedVisibility(visible = isEditing) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                IconButton(
                                    onClick = {
                                        vm.updateGoalsHome(
                                            fixture.id!!,
                                            maxOf((predictionSet.goalsHome ?: 0) - 1, 0)
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                                /*

                                Text(
                                    text = (predictionSet.goalsHome ?: 0).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                                 */

                                IconButton(
                                    onClick = {
                                        vm.updateGoalsHome(
                                            fixture.id!!,
                                            (predictionSet.goalsHome ?: 0) + 1
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // ======================
                    // MARCADOR CENTRADO
                    // ======================
                    Box(
                        modifier = Modifier
                            .weight(0.6f)
                            .scale(scale)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${predictionSet.goalsHome ?: "-"}  :  ${predictionSet.goalsAway ?: "-"}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC857)
                        )
                    }

                    // ======================
                    // GOLES VISITA
                    // ======================
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        AnimatedVisibility(visible = isEditing) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                IconButton(
                                    onClick = {
                                        vm.updateGoalsAway(
                                            fixture.id!!,
                                            maxOf((predictionSet.goalsAway ?: 0) - 1, 0)
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                                /*
                                Text(
                                    text = (predictionSet.goalsAway ?: 0).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                                 */

                                IconButton(
                                    onClick = {
                                        vm.updateGoalsAway(
                                            fixture.id!!,
                                            (predictionSet.goalsAway ?: 0) + 1
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.AirplanemodeActive,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(45f)
                        )
                    }
                }

            }
        }
    }
}