package com.optic.ecommerceappmvvm.presentation.screens.prode.item

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel
import com.optic.ecommerceappmvvm.presentation.ui.theme.getColorBorderFixture
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenColorFixture
import com.optic.ecommerceappmvvm.presentation.ui.theme.getRedColorFixture
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProdeItem(
    fixture: FixtureResponse,
    navController: NavHostController,
    vm: ProdeViewModel
) {

    val predictionSet = vm.userPredictions.collectAsState().value[fixture.id]
        ?: ProdeViewModel.UserPrediction()


    val fixtureDateTime = remember {
        try {
            OffsetDateTime.parse(fixture.date).toLocalDateTime()
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    val currentYear = LocalDateTime.now().year
    val formattedDate = remember(fixtureDateTime) {
        if (fixtureDateTime.year == currentYear) {
            fixtureDateTime.format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))
        } else {
            fixtureDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
            /*
            .clickable {
                fixture.id?.let {
                    navController.navigate("${Graph.FIXTURE}/$it")
                }
            },

             */
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) ,
        shape = RoundedCornerShape(10.dp)  // 👈 bordes redondeados
        //elevation = CardDefaults.cardElevation(1.dp) // sombra opcional

    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Fecha (izquierda) y liga (derecha)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(fixture.league?.logo)
                            .crossfade(true)
                            .build(),
                        contentDescription = fixture.league?.name,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = fixture.league?.name ?: "Unknown League",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }


            }

            Spacer(modifier = Modifier.height(8.dp))

            // Equipos + marcador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Equipo local
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Text(
                        text = fixture.teamHome?.name ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AsyncImage(
                        model = fixture.teamHome?.logo,
                        contentDescription = fixture.teamHome?.name,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                }

                // Marcador centrado
                ScoreBoxAnimated(
                    homeScore = fixture.goalsHome,
                    awayScore = fixture.goalsAway,
                    statusShort = fixture.statusShort,
                    fixtureDate = fixture.date
                )

                // Equipo visitante
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = fixture.teamAway?.logo,
                        contentDescription = fixture.teamAway?.name,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = fixture.teamAway?.name ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                }


            }
        }


// === CONTENEDOR DEL BLOQUE (ANIMADO) PARA EDITAR EL RESULTADO DE LA PREDICCION DEL USUARIO===
    ProdePredictRow(
        fixture = fixture,
        vm = vm
    )

    }
}

@Composable
fun ScoreBoxAnimated(
    homeScore: Int?,
    awayScore: Int?,
    statusShort: String?,
    fixtureDate: String?
) {

    val context = LocalContext.current

    // Si el partido NO ha empezado → mostrar hora HH:mm
    if (statusShort == "NS" && fixtureDate != null) {

        val localTime = try {
            OffsetDateTime.parse(fixtureDate)
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))
        } catch (e: Exception) {
            "--:--"
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f))
                .padding(vertical = 6.dp, horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = localTime,
                /*fontWeight = FontWeight.Bold, */
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        return
    }

    // Si SÍ hay goles → marcador animado
    val green = MaterialTheme.colorScheme.getGreenColorFixture
    val red = MaterialTheme.colorScheme.getRedColorFixture
    val gray = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    val homeBg by animateColorAsState(
        targetValue = when {
            homeScore != null && awayScore != null && homeScore > awayScore -> green
            homeScore != null && awayScore != null && homeScore < awayScore -> red
            else -> gray
        },
        label = "HomeScoreColor"
    )

    val awayBg by animateColorAsState(
        targetValue = when {
            homeScore != null && awayScore != null && awayScore > homeScore -> green
            homeScore != null && awayScore != null && awayScore < homeScore -> red
            else -> gray
        },
        label = "AwayScoreColor"
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f))
    ) {
        Box(
            modifier = Modifier
                .background(homeBg)
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(
                text = (homeScore ?: "-").toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Box(
            modifier = Modifier
                .background(awayBg)
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(
                text = (awayScore ?: "-").toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
