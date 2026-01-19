package com.optic.pramosfootballappz.presentation.screens.team.components.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.presentation.ui.theme.getGreenColorFixture
import com.optic.pramosfootballappz.presentation.ui.theme.getRedColorFixture

@Composable
fun TopTeamFixture(
    topFiveFixtureState: Resource<List<FixtureResponse>>,
    teamId: Int,
    navController: NavHostController
) {

    // para idioma
    val localizedContext = LocalizedContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            // Título
            Text(
                text = localizedContext.getString(R.string.team_detail_screen_ultimos5_title),
                //style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 15.dp)
            )

            when (topFiveFixtureState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                }

                is Resource.Success -> {
                    val fixtures = topFiveFixtureState.data ?: emptyList()

                    if (fixtures.isEmpty()) {
                        Text(
                            "No se encontraron partidos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            fixtures.forEach { fixture ->
                                TopFixtureItem(fixture, teamId, Modifier.weight(1f), navController)
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    Text(
                        text = "Error al cargar los partidos",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
fun TopFixtureItem(
    fixture: FixtureResponse,
    teamId: Int,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val isHome = fixture.teamHome?.id == teamId
    val teamScore = if (isHome) fixture.goalsHome else fixture.goalsAway
    val rivalScore = if (isHome) fixture.goalsAway else fixture.goalsHome
    val rivalLogo = if (isHome) fixture.teamAway?.logo else fixture.teamHome?.logo
    val rivalName = if (isHome) fixture.teamAway?.name else fixture.teamHome?.name

    val green = MaterialTheme.colorScheme.getGreenColorFixture
    val red = MaterialTheme.colorScheme.getRedColorFixture
    val gray = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    val bgColor = when {
        teamScore != null && rivalScore != null && teamScore > rivalScore -> green
        teamScore != null && rivalScore != null && teamScore < rivalScore -> red
        else -> gray
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Resultado
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(bgColor)
                .padding(vertical = 2.dp, horizontal = 6.dp)
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
        ) {
            Text(
                text = "${teamScore ?: "-"}-${rivalScore ?: "-"}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Logo rival
        AsyncImage(
            model = rivalLogo,
            contentDescription = rivalName,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        )
    }
}