package com.optic.ecommerceappmvvm.presentation.screens.matches


import android.os.Build
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.ecommerceappmvvm.presentation.components.LoginLinkCard
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.matches.fixturesbydate.FixturesByDate
import com.optic.ecommerceappmvvm.presentation.ui.theme.IconSecondaryColor



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchesScreen(
    navController: NavHostController,
    isAuthenticated : Boolean
) {
    val viewModel: MatchesViewModel = hiltViewModel()
    val limit = viewModel.limit
    val fixtureState by viewModel.fixtureTeamsState.collectAsState()
    val fixtureStateDate by viewModel.fixtureDateState.collectAsState()



    // fecha real de hoy
    val today = LocalDate.now()
    //val backStackEntry = navController.currentBackStackEntryAsState().value

    // estado para la fecha seleccionada (local)
    var selectedDate by remember { mutableStateOf(today) }
    var previousDate by remember { mutableStateOf(today) }

    val backStackEntry by navController.currentBackStackEntryAsState()

    val selectedDateFromCalendar =
        backStackEntry
            ?.savedStateHandle
            ?.getStateFlow<LocalDate?>("selected_date", null)
            ?.collectAsState()
    if (selectedDateFromCalendar?.value != null ) {
        LaunchedEffect(selectedDateFromCalendar?.value) {
            val newDate = selectedDateFromCalendar?.value ?: return@LaunchedEffect
            selectedDate = newDate
            previousDate = newDate

            viewModel.getFixtureFollowedTeams(2025, newDate.toString())
            viewModel.getFixturesByDate(newDate.toString(), limit)
            // Limpia después de usarlo
            backStackEntry
                ?.savedStateHandle
                ?.set("selected_date", null)
        }
    }else {
        // cargar datos iniciales

        LaunchedEffect(backStackEntry?.destination?.route) {
            viewModel.getFixtureFollowedTeams(season = 2025, date = today.toString())
            viewModel.getFixturesByDate(date = today.toString(), limit = limit)
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
                modifier = Modifier.fillMaxSize())
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()   // ocupa todo el alto y ancho disponible
                        .padding(paddingValues)
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Top // los ítems empiezan arriba

                ) {

                    if (isAuthenticated) {
                        item {
                            FollowFixtureList(
                                navController = navController,
                                fixtureState = fixtureState
                            )
                        }

                        item {
                            FixturesByDate(
                                navController = navController,
                                fixtureState = fixtureStateDate
                            )
                        }

                    } else {

                        item {
                            LoginLinkCard(
                                navController = navController
                            )
                        }

                        item {
                            FixturesByDate(
                                navController = navController,
                                fixtureState = fixtureStateDate,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
