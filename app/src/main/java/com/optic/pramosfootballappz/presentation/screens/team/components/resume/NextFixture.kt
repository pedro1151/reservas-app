package com.optic.pramosfootballappz.presentation.screens.team.components.resume

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse
import com.optic.pramosfootballappz.domain.util.Resource
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.presentation.ui.theme.getGreenColorFixture
import com.optic.pramosfootballappz.presentation.ui.theme.getRedColorFixture
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextFixture(
    title: String = "Próximo partido",
    state: Resource<FixtureResponse>,
    navController: NavHostController
) {

    // para idioma
    val localizedContext = LocalizedContext.current

    when (state) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is Resource.Success -> {
            state.data?.let { fixture ->
                NextFixtureItem(
                    fixture = fixture,
                    title = localizedContext.getString(R.string.team_detail_screen_proximo_partido_title),
                    navController
                )
            } ?: Text(
                "No se ha encontrado el próximo partido",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        is Resource.Failure -> {
            Text(
                "No se ha encontrado el próximo partido",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextFixtureItem(
    fixture: FixtureResponse,
    title: String = "Próximo partido",
    navController: NavHostController
) {
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
        modifier = Modifier.fillMaxWidth()
            .clickable {
                fixture.id?.let { id ->
                    navController.navigate("${Graph.FIXTURE}/$id") {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Título en esquina superior izquierda
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 15.dp)
            )

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
                    AsyncImage(
                        model = fixture.teamHome?.logo,
                        contentDescription = fixture.teamHome?.name,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = fixture.teamHome?.name ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Marcador centrado
                ScoreBoxAnimated(
                    homeScore = fixture.goalsHome,
                    awayScore = fixture.goalsAway
                )

                // Equipo visitante
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fixture.teamAway?.name ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AsyncImage(
                        model = fixture.teamAway?.logo,
                        contentDescription = fixture.teamAway?.name,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreBoxAnimated(homeScore: Int?, awayScore: Int?) {
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