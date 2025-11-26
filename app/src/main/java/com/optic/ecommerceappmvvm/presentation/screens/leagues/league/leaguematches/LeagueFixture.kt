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
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {}

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            when (fixtureState) {

                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {

                    val fixtures = fixtureState.data ?: emptyList()
                    val listState = rememberLazyListState()

                    val today = LocalDate.now()

                    // Convertir fecha del fixture a LocalDate
                    fun String?.toLocalDateOrNull(): LocalDate? =
                        try { this?.let { OffsetDateTime.parse(it).toLocalDate() } }
                        catch (_: Exception) { null }

                    // Extraer fechas válidas
                    val fixtureDates = fixtures.mapNotNull { it.date.toLocalDateOrNull() }

                    // 1. Buscar fechas exactas de hoy
                    val todayIndexes = fixtures.mapIndexedNotNull { index, fixture ->
                        if (fixture.date.toLocalDateOrNull() == today) index else null
                    }

                    // 2. Si hay partidos hoy usamos ese índice
                    val targetIndex = if (todayIndexes.isNotEmpty()) {
                        todayIndexes.first()
                    } else {
                        // 3. Si NO hay partidos hoy → encontrar la fecha más cercana
                        val nearestDate = fixtureDates.minByOrNull {
                            kotlin.math.abs(ChronoUnit.DAYS.between(today, it))
                        }

                        // 4. Buscar el primer fixture con esa fecha más cercana
                        fixtures.indexOfFirst {
                            it.date.toLocalDateOrNull() == nearestDate
                        }.takeIf { it >= 0 } ?: 0
                    }

                    // Scroll automático centrado
                    LaunchedEffect(targetIndex) {
                        if (fixtures.isNotEmpty()) {
                            // Esperar a que el LazyColumn tenga layout
                            repeat(10) {
                                if (listState.layoutInfo.totalItemsCount > 0) return@repeat
                                kotlinx.coroutines.delay(30)
                            }

                            // Altura aproximada del item (ajústala según tu UI)
                            val itemHeightPx = 100

                            val viewportHeight = listState.layoutInfo.viewportEndOffset -
                                    listState.layoutInfo.viewportStartOffset

                            val centerOffset = (viewportHeight / 2) - (itemHeightPx / 2)
                            delay(500) // deja que la lista se pinte primero
                            listState.animateScrollToItem(
                                targetIndex,
                                centerOffset.coerceAtLeast(0)
                            )
                        }
                    }

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        items(fixtures) { fixture ->
                            FixtureItem(
                                fixture = fixture,
                                navController = navController
                            )
                        }
                    }
                }

                is Resource.Failure -> {
                    Text(
                        text = "Error al cargar los Partidos",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}
