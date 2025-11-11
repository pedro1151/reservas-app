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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.navigation.compose.rememberNavController
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.home.components.ClientBottomBar
import com.optic.ecommerceappmvvm.presentation.screens.matches.countryfixtures.CountryFixtures
import com.optic.ecommerceappmvvm.presentation.screens.matches.nofollowfixtures.NoFollowFixtures

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MatchesScreen(navController: NavHostController) {
    val viewModel: MatchesViewModel = hiltViewModel()
    val fixtureState by viewModel.fixtureTeamsState.collectAsState()
    val fixtureStateCountry by viewModel.fixtureCountryState.collectAsState()
    val fixtureStateNoFollow by viewModel.fixturesNoFollow.collectAsState()


    val backStackEntry = navController.currentBackStackEntryAsState().value
    val fakeToday = LocalDate.of(2023, 9, 17)

    // estado para la fecha seleccionada
    var selectedDate by remember { mutableStateOf(fakeToday) }
    var previousDate by remember { mutableStateOf(fakeToday) }

    // cargar datos iniciales
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixtureFollowedTeams(
            season = 2023,
            date = fakeToday.toString()
        )
    }

    // cargar datos iniciales no follow
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getNoFixtureFollowedTeams(
            season = 2023,
            date = fakeToday.toString()
        )
    }

    // cargar datos iniciales
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixturesByCountry(
            season = 2023,
            date = fakeToday.toString()
        )
    }


    Scaffold(
        topBar = {
            Column {
                PrimaryTopBar(
                    navController = navController,
                    title = "SMARTFOOT"
                )
                MatchesDateTopBar { newDate ->
                    previousDate = selectedDate
                    selectedDate = newDate
                    viewModel.getFixtureFollowedTeams(
                        season = 2023,
                        date = newDate.toString()
                    )

                    viewModel.getNoFixtureFollowedTeams(
                        season = 2023,
                        date = newDate.toString()
                    )

                    viewModel.getFixturesByCountry(
                        season = 2023,
                        date = newDate.toString()
                    )
                }
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
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()   // ocupa todo el alto y ancho disponible
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top // los ítems empiezan arriba
            ) {
                item {
                    FollowFixtureList(
                        navController = navController,
                        fixtureState = fixtureState
                    )
                }

                item {
                    NoFollowFixtures(
                        navController = navController,
                        fixtureState = fixtureStateNoFollow
                    )
                }

                item {
                    CountryFixtures(
                        navController = navController,
                        fixtureState = fixtureStateCountry,
                        modifier = Modifier.fillParentMaxHeight() // 👈 ocupa todo el resto
                    )
                }
            }
        }
    }
}
