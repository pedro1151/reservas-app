package com.optic.ecommerceappmvvm.presentation.screens.leagues.league.leaguematches


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.item.FixtureItem
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeagueFixture(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    fixtureState: Resource<List<FixtureResponse>>,
    title: String = "Siguiendo"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 1.dp, vertical = 8.dp)
    ) {

        when (fixtureState) {

            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {

                val fixtures = fixtureState.data.orEmpty()

                // ---------- Helpers ----------
                fun String?.toLocalDateOrNull(): LocalDate? =
                    try {
                        this?.let { OffsetDateTime.parse(it).toLocalDate() }
                    } catch (_: Exception) {
                        null
                    }

                // ---------- Agrupar por fecha ----------
                val groupedFixtures = fixtures
                    .mapNotNull { fixture ->
                        fixture.date.toLocalDateOrNull()?.let { date ->
                            date to fixture
                        }
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                    .toSortedMap() // 👈 orden cronológico

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    groupedFixtures.forEach { (date, fixturesForDay) ->

                        item(key = date.toString()) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                   // .padding(horizontal = 10.dp, vertical = 10.dp ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                               // elevation = CardDefaults.cardElevation(1.dp), // sombra opcional
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column {

                                    // ---------- HEADER FECHA ----------
                                    Text(
                                        text = formatFixtureDate(date),
                                     //   style = MaterialTheme.typography.,
                                        modifier = Modifier
                                            .padding(
                                                start = 16.dp,
                                                top = 12.dp,
                                                bottom = 8.dp
                                            )
                                    )


                                    // ---------- FIXTURES ----------
                                    fixturesForDay.forEach { fixture ->
                                        FixtureItem(
                                            fixture = fixture,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is Resource.Failure -> {
                Text(
                    text = "Error al cargar los partidos",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {}
        }
    }
}
fun formatFixtureDate(date: LocalDate): String {
    val today = LocalDate.now()
    val currentYear = today.year

    return when (date) {
        today -> "Hoy"
        today.plusDays(1) -> "Mañana"
        today.minusDays(1) -> "Ayer"
        else -> {
            val pattern = if (date.year != currentYear) {
                "EEEE d MMM, yyyy"
            } else {
                "EEEE d MMM"
            }

            date.format(
                java.time.format.DateTimeFormatter.ofPattern(
                    pattern,
                    java.util.Locale("es")
                )
            ).replaceFirstChar { it.uppercase() }
        }
    }
}

