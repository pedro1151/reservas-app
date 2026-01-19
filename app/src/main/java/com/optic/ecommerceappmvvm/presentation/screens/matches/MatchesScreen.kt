package com.optic.ecommerceappmvvm.presentation.screens.matches


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.datetopbar.MatchesDateTopBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.folllowfixtures.FollowFixtureList
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight
import java.time.LocalDate
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.optic.ecommerceappmvvm.presentation.components.LoginLinkCard
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.matches.fixturesbydate.FixturesByDate
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchesScreen(
    navController: NavHostController,
    isAuthenticated : Boolean,
    onShowRewardAd: () -> Unit
) {
    val viewModel: MatchesViewModel = hiltViewModel()
    val limit = viewModel.limit
    val fixtureState by viewModel.fixtureTeamsState.collectAsState()
    val fixtureStateDate by viewModel.fixtureDateState.collectAsState()



    // fecha real de hoy
    val today = LocalDate.now()

    // estado para la fecha seleccionada (local)
    var selectedDate by remember { mutableStateOf(today) }
    var previousDate by remember { mutableStateOf(today) }

    val backStackEntry by navController.currentBackStackEntryAsState()

    val selectedDateFromCalendar =
        backStackEntry
            ?.savedStateHandle
            ?.getStateFlow<LocalDate?>("selected_date", null)
            ?.collectAsState()


    LaunchedEffect(selectedDateFromCalendar?.value) {

        val calendarDate = selectedDateFromCalendar?.value

        if (calendarDate != null) {
            // Cambio por calendario
            selectedDate = calendarDate
            previousDate = calendarDate

            viewModel.getFixtureFollowedTeams(2025, calendarDate.toString())
            viewModel.getFixturesByDate(calendarDate.toString(), limit)

            backStackEntry?.savedStateHandle?.set("selected_date", null)

            Log.d("getFixturesByDate", "CARGA: Fecha del calendario → $calendarDate")

        } else if (selectedDate == today) {
            // Cargar solo una vez al inicio
            viewModel.getFixtureFollowedTeams(2025, today.toString())
            viewModel.getFixturesByDate(today.toString(), limit)

            Log.d("getFixturesByDate", "CARGA: Fecha hoy por defecto → $today")
        }
    }

    Scaffold(
        topBar = {
            Column {
                PrimaryTopBar(
                    title = "UNIFOT",
                    navController = navController,
                    onCalendarClick = {
                        navController.navigate(ClientScreen.Calendar.route)
                    }
                )

                MatchesDateTopBar(
                    selectedDate = selectedDate,
                    onDateSelected = { newDate ->
                        previousDate = selectedDate
                        selectedDate = newDate

                        viewModel.getFixtureFollowedTeams(2025, newDate.toString())
                       // viewModel.getFixturesByCountry(2025, newDate.toString())
                        viewModel.getFixturesByDate(newDate.toString(), limit)
                    }
                )


            }
        },
        containerColor = GreyLight
    ) { paddingValues ->

        AnimatedContent(
            targetState = selectedDate,
            transitionSpec = {
                if (targetState > initialState) {
                    // futura → desliza hacia la izquierda
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                } else {
                    // pasada → desliza hacia la derecha
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )
                }
            },
            label = "FixturesAnimation"
        ) { _ ->
            Column(
                modifier = Modifier.fillMaxSize()
                        .fillMaxSize()   // ocupa todo el alto y ancho disponible
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Top // los ítems empiezan arriba

                ) {

                            FixturesByDate(
                                navController = navController,
                                fixtureState = fixtureStateDate,
                                followedFixtureState = fixtureState,
                                modifier = Modifier.fillMaxSize(),
                                isAuthenticated = isAuthenticated
                            )


                }

        }
    }
}
